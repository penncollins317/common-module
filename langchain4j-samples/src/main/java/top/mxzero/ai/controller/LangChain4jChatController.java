package top.mxzero.ai.controller;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.mxzero.ai.config.OpenAiChatOptions;
import top.mxzero.ai.config.OpenAiConfig;
import top.mxzero.ai.service.ChatAssistant;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * AI聊天服务
 *
 * @author Peng
 * @since 2025/5/7
 */
@AllArgsConstructor
@RequestMapping("/langchain4j")
@RestController
@Slf4j
public class LangChain4jChatController {
    private final ExecutorService executorService;
    private final ChatMemoryStore chatMemoryStore;
    private final OpenAiConfig openAiConfig;
    private final OpenAiChatOptions openAiChatOptions;

    private OpenAiStreamingChatModel openAiStreamingChatModel(String modelName) {
        return OpenAiStreamingChatModel.builder()
                .apiKey(openAiConfig.getApiKey())
                .baseUrl(openAiConfig.getBaseUrl())
                .modelName(modelName != null ? modelName : openAiChatOptions.getModel())
                .temperature(openAiChatOptions.getTemperature())
                .build();
    }

    /**
     * 文字对话
     */
    @RequestMapping("/chat")
    public SseEmitter streamChatApi(
            @RequestParam(value = "content") String content,
            @RequestParam(value = "chat_conversation_id") String chatConversationId,
            @RequestParam(value = "model", required = false) String modelName
    ) {
        ChatAssistant assistant = AiServices.builder(ChatAssistant.class)
                .streamingChatModel(this.openAiStreamingChatModel(modelName))
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(openAiChatOptions.getMaxHistoryMsg())
                        .chatMemoryStore(chatMemoryStore)
                        .build())
                .build();
        SseEmitter emitter = new SseEmitter();
        executorService.submit(() -> {
            TokenStream tokenStream = assistant.chatStream(chatConversationId, content);
            tokenStream.onError((throwable) -> {
                log.error(throwable.getMessage());
                emitter.completeWithError(throwable);
            }).onCompleteResponse((chatResponse) -> {
                emitter.complete();
            }).onPartialResponse((s) -> {
                SseEmitter.SseEventBuilder event = SseEmitter.event()
                        .data(Map.of("s", s)) // 事件数据
                        .name("message") // 事件名称
                        .reconnectTime(5000); // 重连时间(毫秒)
                try {
                    emitter.send(event);
                } catch (IOException e) {
                    log.warn(e.getMessage());
                }
            }).start();
        });
        return emitter;
    }


}