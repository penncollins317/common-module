package top.echovoid.ai.controller;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.echovoid.ai.config.OpenAiChatOptions;
import top.echovoid.ai.config.OpenAiConfig;
import top.echovoid.ai.entity.AiChatConversation;
import top.echovoid.ai.service.AiChatConversationService;
import top.echovoid.ai.service.ChatAssistant;
import top.echovoid.common.annotations.AuthenticatedRequired;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * AI聊天服务
 *
 * @author Penn Collins
 * @since 2025/5/7
 */
@AuthenticatedRequired
@AllArgsConstructor
@RequestMapping("/langchain4j")
@RestController
@Slf4j
public class LangChain4jChatController {
    private final ExecutorService executorService;
    private final ChatMemoryStore chatMemoryStore;
    private final OpenAiConfig openAiConfig;
    private final OpenAiChatOptions openAiChatOptions;
    private final AiChatConversationService conversationService;

    private OpenAiChatModel openAiChatModel(String modelName) {
        return OpenAiChatModel.builder()
                .apiKey(openAiConfig.getApiKey())
                .baseUrl(openAiConfig.getBaseUrl())
                .modelName(StringUtils.hasLength(modelName) ? modelName : openAiChatOptions.getModel())
                .build();
    }

    private OpenAiStreamingChatModel openAiStreamingChatModel(String modelName) {
        return OpenAiStreamingChatModel.builder()
                .apiKey(openAiConfig.getApiKey())
                .baseUrl(openAiConfig.getBaseUrl())
                .modelName(StringUtils.hasLength(modelName) ? modelName : openAiChatOptions.getModel())
                .temperature(openAiChatOptions.getTemperature())
                .build();
    }

    /**
     * 文字对话
     */
    @RequestMapping("/chat")
    public SseEmitter streamChatApi(
            @RequestParam(value = "content") String content,
            @RequestParam(value = "chat_conversation_id") String conversationId,
            @RequestParam(value = "model", required = false) String modelName,
            Principal principal
    ) {
        SseEmitter emitter = new SseEmitter();
        AiChatConversation conversation = conversationService.getById(conversationId);
        if (conversation == null || !conversation.getUserId().equals(Long.valueOf(principal.getName()))) {
            SseEmitter.SseEventBuilder event = SseEmitter.event()
                    .data(Map.of("v", "会话不存在"))
                    .name("error");
            try {
                emitter.send(event);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            emitter.complete();
            return emitter;
        }
        ChatAssistant assistant = AiServices.builder(ChatAssistant.class)
                .streamingChatModel(this.openAiStreamingChatModel(modelName))
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(openAiChatOptions.getMaxHistoryMsg())
                        .chatMemoryStore(chatMemoryStore)
                        .build())
                .build();

        Future<String> future = null;

        if (!StringUtils.hasLength(conversation.getTitle())) {
            future = executorService.submit(() -> {
                String titleResult = openAiChatModel(openAiChatOptions.getModel()).chat("根据以下对话生成不超过20字的标题，优先突出用户意图，标题要求：简明扼要，不要引号，内容如下：" + content);
                log.info("titleResult:{}", titleResult);
                return titleResult;
            });
        }

        Future<String> finalFuture = future;
        executorService.submit(() -> {
            TokenStream tokenStream = assistant.chatStream(conversation.getConversationId(), content);
            tokenStream.onError((throwable) -> {
                log.error(throwable.getMessage());
                try {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data(Map.of("v", "响应错误"))
                            .name("error");
                    emitter.send(event);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                emitter.completeWithError(throwable);
            }).onCompleteResponse((chatResponse) -> {
                if (finalFuture != null) {
                    try {
                        String titleResult = finalFuture.get(2, TimeUnit.SECONDS);
                        SseEmitter.SseEventBuilder event = SseEmitter.event()
                                .data(Map.of("v", titleResult)) // 事件数据
                                .name("title"); // 事件名称
                        emitter.send(event);
                        conversation.setTitle(titleResult);
                        conversationService.changeConversationTitle(conversationId, Long.valueOf(principal.getName()), titleResult);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
                try {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data(Map.of("v", "")) // 事件数据
                            .name("completed"); // 事件名称
                    emitter.send(event);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                emitter.complete();
            }).onPartialResponse((s) -> {
                SseEmitter.SseEventBuilder event = SseEmitter.event()
                        .data(Map.of("v", s)) // 事件数据
                        .name("message"); // 事件名称
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