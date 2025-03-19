package top.mxzero.ai.controller;

import jakarta.validation.Valid;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.mxzero.ai.dto.request.ChatInputDTO;
import top.mxzero.ai.service.AiConversationService;
import top.mxzero.common.exceptions.ServiceException;
import top.mxzero.security.core.annotations.AuthenticatedRequired;

import java.security.Principal;
import java.util.List;

/**
 * AI聊天
 *
 * @author Peng
 * @since 2024/11/27
 */
@AuthenticatedRequired
@RestController
@RequestMapping("/ai/chat")
public class AiChatController {
    private final ChatModel model;
    private final ChatClient client;
    private final AiConversationService conversationService;
    private static final String modelName = "deepseek-ai/DeepSeek-R1-Distill-Qwen-7B";

    public AiChatController(ChatModel model, ChatMemory chatMemory, AiConversationService conversationService) {
        this.conversationService = conversationService;
        this.model = model;
        this.client = ChatClient.builder(this.model).defaultAdvisors(new PromptChatMemoryAdvisor(chatMemory)).build();
    }

    /**
     * AI流式消息
     *
     * @param inputDTO 用户输入内容
     */

    @PostMapping(value = "/stream")
    public Flux<ServerSentEvent<String>> defaultChatModelStreamApi(
            @Valid @RequestBody ChatInputDTO inputDTO,
            Principal principal
    ) {
        if (!this.conversationService.existsConversation(inputDTO.getConversationId(), Long.valueOf(principal.getName()))) {
            throw new ServiceException("会话不存在");
        }
        Flux<ServerSentEvent<String>> messageEvents = this.client.prompt(new Prompt(inputDTO.getContent()))
                .advisors(advisorSpec -> {
                    advisorSpec.param("chat_memory_conversation_id", inputDTO.getConversationId());
                })
                .stream().chatResponse()
                .filter(data -> StringUtils.hasLength(data.getResult().getOutput().getText()))
                .map(data -> ServerSentEvent.<String>builder()
                        .event("message")
                        .data(data.getResult().getOutput().getText())
                        .build()
                );
        Mono<ServerSentEvent<String>> closeEvent = Mono.just(
                ServerSentEvent.<String>builder().event("complete").data("").build()
        );

        return messageEvents
                .concatWith(closeEvent)
                .onErrorResume(e -> Flux.just(
                        ServerSentEvent.<String>builder()
                                .event("error")
                                .data(e.getMessage())
                                .build()
                ).concatWith(closeEvent));
    }

    @RequestMapping(value = "stream/model")
    public Flux<ServerSentEvent<String>> chatStreamWithModelApi(@RequestParam("msg") String content, @RequestParam(value = "model") String modelName, @RequestParam(value = "temperature", defaultValue = "1.0") Double temperature) {
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(modelName)
                .temperature(temperature)
                .build();

        Prompt prompt = new Prompt(content, options);

        Flux<ChatResponse> openAiStream = this.model.stream(prompt);
        Flux<ServerSentEvent<String>> messageEvents = openAiStream
                .map(data -> ServerSentEvent.<String>builder()
                        .event("message")
                        .data(data.getResult().getOutput().getText())
                        .build()
                );
        Mono<ServerSentEvent<String>> closeEvent = Mono.just(
                ServerSentEvent.<String>builder().event("close").data("").build()
        );

        return messageEvents
                .concatWith(closeEvent)
                .onErrorResume(e -> Flux.just(
                        ServerSentEvent.<String>builder()
                                .event("error")
                                .data(e.getMessage())
                                .build()
                ).concatWith(closeEvent));
    }

    @RequestMapping(value = "/reasoner")
    public Flux<ServerSentEvent<String>> reasonerChatApi(@RequestParam("content") String content) {
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(modelName)
                .temperature(this.model.getDefaultOptions().getTemperature())
                .build();

        // ==================== 第一轮推理 ====================
        // 先将 ChatResponse 转换成 ServerSentEvent<String>
        Flux<ServerSentEvent<String>> mappedFirstPhase = this.model.stream(new Prompt(new UserMessage(content), options))
                .filter(data -> StringUtils.hasText(data.getResult().getOutput().getText()))
                .map(data -> ServerSentEvent.<String>builder()
                        .event("message")
                        .data(data.getResult().getOutput().getText())
                        .build());

        // 在转换后的流前插入起始标记，后追加结束标记
        Flux<ServerSentEvent<String>> firstPhase = mappedFirstPhase
                .startWith(ServerSentEvent.<String>builder()
                        .event("first_chunk")
                        .data("")
                        .build())
                .concatWith(Flux.just(ServerSentEvent.<String>builder()
                        .event("first_end")
                        .data("")
                        .build()));

        // ==================== 第二轮推理 ====================
        Flux<ServerSentEvent<String>> secondPhase = firstPhase
                // 收集第一轮完整结果
                .collect(StringBuilder::new, (sb, event) -> {
                    if ("message".equals(event.event())) {
                        sb.append(event.data());
                    }
                })
                // 触发第二轮推理
                .flatMapMany(firstPhaseResult ->
                        this.model.stream(new Prompt(List.of(
                                        new AssistantMessage(firstPhaseResult.toString()),
                                        new UserMessage(content)
                                ), options))
                                // 转换为 ServerSentEvent 流
                                .filter(data -> StringUtils.hasText(data.getResult().getOutput().getText()))
                                .map(data -> ServerSentEvent.<String>builder()
                                        .event("message")
                                        .data(data.getResult().getOutput().getText())
                                        .build())
                                // 插入第二轮起始标记和结束标记
                                .startWith(ServerSentEvent.<String>builder()
                                        .event("second_chunk")
                                        .data("")
                                        .build())
                                .concatWith(Flux.just(ServerSentEvent.<String>builder()
                                        .event("second_end")
                                        .data("")
                                        .build()))
                );

        // ==================== 合并流并处理结束 ====================
        return Flux.concat(firstPhase, secondPhase)
                // 发送全局完成标记
                .concatWith(Flux.just(ServerSentEvent.<String>builder()
                        .event("complete")
                        .data("")
                        .build()))
                // 错误处理
                .onErrorResume(e -> Flux.just(
                        ServerSentEvent.<String>builder()
                                .event("error")
                                .data(e.getMessage())
                                .build()
                ));
    }

}
