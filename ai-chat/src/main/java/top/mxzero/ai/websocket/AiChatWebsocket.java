package top.mxzero.ai.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import top.mxzero.ai.service.AssistMessageChatMemory;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@Component
public class AiChatWebsocket extends TextWebSocketHandler {

    private final ChatClient chatClient;

    public AiChatWebsocket(ChatClient.Builder builder,
                           AssistMessageChatMemory chatMemory) {
        this.chatClient = builder.defaultAdvisors(new PromptChatMemoryAdvisor(chatMemory)).build();
    }

    // WebSocket 连接建立后触发的事件
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String conversationId = null;
        URI uri = session.getUri();
        if (uri != null) {
            conversationId = UriComponentsBuilder.fromUri(session.getUri()).build().getQueryParams().getFirst("conversation_id");
        }

        if (conversationId == null) {
            conversationId = UUID.randomUUID().toString();
        }

        session.getAttributes().put("conversationId", conversationId);

        log.info("WebSocket connection established with conversation id:: {}", conversationId);
    }

    // 处理收到的消息
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 从消息中提取用户输入
        String userMessage = message.getPayload();
        log.info("Received message: {}", userMessage);

        // 获取当前会话的 sessionId
        String conversationId = (String) session.getAttributes().get("conversationId");

        // 获取 AI 流式回复，并逐步发送到客户端
        handleAiStream(session, conversationId, userMessage);
    }

    // 使用 StringBuilder 缓冲 AI 流式回复，处理完毕后存储消息
    private void handleAiStream(WebSocketSession session, String conversationId, String userMessage) {
        try {
            // 先发送开始标记
            session.sendMessage(new TextMessage("wsmsg:start"));
            ChatClient.ChatClientRequestSpec prompt = this.chatClient.prompt()
                    .advisors(advisorSpec -> {
                        advisorSpec.param("chat_memory_conversation_id", conversationId);
                    })
                    .user(userMessage);
            ChatClient.StreamResponseSpec stream = prompt.stream();
            // 监听并逐步处理流中的数据
            stream.content()
                    .delayElements(Duration.ofMillis(50))  // 控制流的发送间隔
                    .doOnNext(text -> {
                        try {
                            if (text != null && !text.isEmpty()) {
                                // 发送流中的每一部分消息
                                session.sendMessage(new TextMessage(text));
                            }
                        } catch (Exception e) {
                            log.error("Error sending message to WebSocket: {}", e.getMessage());
                            try {
                                session.sendMessage(new TextMessage("Sorry, an error occurred while processing your request."));
                            } catch (IOException ex) {
                                log.error(ex.getMessage());
                            }
                            // 取消订阅
                            stream.content().blockLast();  // 阻塞直到流完全完成
                        }
                    })
                    .onErrorResume(e -> {
                        // 捕获错误并发送错误信息
                        log.error("Stream error: {}", e.getMessage());
                        try {
                            session.sendMessage(new TextMessage("Sorry, an error occurred while processing your request."));
                        } catch (Exception ex) {
                            log.error("Error sending error message to WebSocket: {}", ex.getMessage());
                        }
                        // 取消流的订阅
                        stream.content().blockLast();  // 阻塞直到流完全完成，防止继续处理流
                        return Mono.empty();  // 返回一个空的 Mono，表示错误处理完成，流已终止
                    })
                    .doOnTerminate(() -> {
                        log.info("AI stream has terminated.");
                        // 发送结束标记
                        try {
                            session.sendMessage(new TextMessage("wsmsg:end"));
                        } catch (IOException e) {
                            log.error("Error sending end message: {}", e.getMessage());
                        }
                    })
                    .subscribe(); // 启动流式接收处理
        } catch (Exception e) {
            log.error("Error while getting AI stream response: {}", e.getMessage());
            try {
                session.sendMessage(new TextMessage("Sorry, I couldn't process your request."));
            } catch (Exception ex) {
                log.error("Error sending error message to WebSocket: {}", ex.getMessage());
            }
        }
    }

    // 处理 WebSocket 异常
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket transport error: {}", exception.getMessage());
        session.close();
    }

    // 处理 WebSocket 连接关闭事件
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebSocket connection closed with session id: {} and status: {}", session.getId(), status);
    }
}
