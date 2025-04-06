package top.mxzero.ai.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.mxzero.ai.dto.request.ChatInputDTO;
import top.mxzero.ai.dto.response.AiChatMessageDTO;
import top.mxzero.ai.service.AiConversationService;
import top.mxzero.ai.tools.DateTools;
import top.mxzero.ai.tools.UserTools;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.exceptions.ServiceErrorCode;
import top.mxzero.common.exceptions.ServiceException;
import top.mxzero.common.utils.UUIDv7Generator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * AI聊天
 *
 * @author Peng
 * @since 2024/11/27
 */
@RestController
@RequestMapping("/ai/chat")
public class AiChatController {
    private final ChatModel chatModel;
    private final ChatMemory chatMemory;
    private final ChatClient client;
    private final AiConversationService conversationService;
    private static final String modelName = "deepseek-ai/DeepSeek-R1-Distill-Qwen-7B";
    private static final String DEFAULT_AI_CHAT_CONVERSATION = "DEFAULT_AI_CHAT_CONVERSATION";

    @Value("classpath:prompts/welcome.st")
    private Resource resource;

    public AiChatController(ChatModel chatModel, ChatMemory chatMemory, AiConversationService conversationService) {
        this.conversationService = conversationService;
        this.chatModel = chatModel;
        this.chatMemory = chatMemory;
        this.client = this.buildChatClient(this.chatModel);
    }

    private ChatClient buildChatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel).defaultAdvisors(new PromptChatMemoryAdvisor(this.chatMemory))
                .build();
    }

    /**
     * 同步调用
     *
     * @param inputDTO 用户输入数据
     */
    @RequestMapping("/call")
    public RestData<Object> callChatApi(
            @Valid ChatInputDTO inputDTO,
            HttpSession session,
            Principal principal) {
        if (StringUtils.hasLength(inputDTO.getConversationId()) && !this.conversationService.existsConversation(inputDTO.getConversationId(), Long.valueOf(principal.getName()))) {
            throw new ServiceException("会话不存在");
        }

        // 为传递conversationId，使用当前session
        if (!StringUtils.hasLength(inputDTO.getConversationId())) {
            String conversationId = (String) session.getAttribute(DEFAULT_AI_CHAT_CONVERSATION);
            if (!StringUtils.hasLength(conversationId)) {
                conversationId = UUIDv7Generator.generateStr();
                session.setAttribute(DEFAULT_AI_CHAT_CONVERSATION, conversationId);
            }
            inputDTO.setConversationId(conversationId);
        }
        ChatResponse response =   this.client.prompt(new Prompt(new UserMessage(inputDTO.getContent())))
                .tools(new DateTools(), new UserTools())
                .advisors(advisorSpec -> {
                    advisorSpec.param("chat_memory_conversation_id", inputDTO.getConversationId());
                }).call().chatResponse();
        return RestData.success(response);
    }

    /**
     * AI流式消息
     *
     * @param inputDTO 用户输入内容
     */
    @RequestMapping("/stream")
    public Flux<ServerSentEvent<String>> defaultChatModelStreamApi(
            @Valid @RequestBody ChatInputDTO inputDTO,
            HttpSession session,
            Principal principal
    ) throws IOException {
        if (StringUtils.hasLength(inputDTO.getConversationId()) && !this.conversationService.existsConversation(inputDTO.getConversationId(), Long.valueOf(principal.getName()))) {
            throw new ServiceException("会话不存在");
        }

        // 为传递conversationId，使用当前session
        if (!StringUtils.hasLength(inputDTO.getConversationId())) {
            String conversationId = (String) session.getAttribute(DEFAULT_AI_CHAT_CONVERSATION);
            if (!StringUtils.hasLength(conversationId)) {
                conversationId = UUIDv7Generator.generateStr();
                session.setAttribute(DEFAULT_AI_CHAT_CONVERSATION, conversationId);
            }
            inputDTO.setConversationId(conversationId);
        }

        List<Message> messages = new ArrayList<>();
        if (this.conversationService.msgCnt(inputDTO.getConversationId()) == 0) {
            String systemPrompt = resource.getContentAsString(StandardCharsets.UTF_8);
            messages.add(new SystemMessage(systemPrompt));
        }
        messages.add(new UserMessage(inputDTO.getContent()));
        Flux<ServerSentEvent<String>> messageEvents = this.client.prompt(new Prompt(messages))
                .advisors(advisorSpec -> {
                    advisorSpec.param("chat_memory_conversation_id", inputDTO.getConversationId());
                })
                .stream().chatResponse()
                .map(data -> ServerSentEvent.<String>builder()
                        .event("message")
                        .data(data.getResult().getOutput().getText())
                        .build()
                );
        Mono<ServerSentEvent<String>> closeEvent = Mono.just(
                ServerSentEvent.<String>builder().event("complete").data("").build()
        );

        return messageEvents
                .startWith(ServerSentEvent.<String>builder()
                        .event("chat_output")
                        .data("")
                        .build())
                .concatWith(closeEvent)
                .onErrorResume(e -> Flux.just(
                        ServerSentEvent.<String>builder()
                                .event("error")
                                .data(e.getMessage())
                                .build()
                ).concatWith(closeEvent));
    }

    @RequestMapping(value = "stream/model")
    public Flux<ServerSentEvent<String>> chatStreamWithModelApi(
            @Valid ChatInputDTO inputDTO,
            HttpSession session,
            Principal principal,
            @RequestParam(value = "model") String modelName) {
        if (StringUtils.hasLength(inputDTO.getConversationId()) && !this.conversationService.existsConversation(inputDTO.getConversationId(), Long.valueOf(principal.getName()))) {
            throw new ServiceException("会话不存在");
        }
        // 为传递conversationId，使用当前session
        if (!StringUtils.hasLength(inputDTO.getConversationId())) {
            String conversationId = (String) session.getAttribute(DEFAULT_AI_CHAT_CONVERSATION);
            if (!StringUtils.hasLength(conversationId)) {
                conversationId = UUIDv7Generator.generateStr();
                session.setAttribute(DEFAULT_AI_CHAT_CONVERSATION, conversationId);
            }
            inputDTO.setConversationId(conversationId);
        }

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(modelName)
                .temperature(this.chatModel.getDefaultOptions().getTemperature())
                .build();

        Prompt prompt = new Prompt(inputDTO.getContent(), options);

        ChatClient.StreamResponseSpec openAiStream = this.client.prompt(prompt)
                .advisors(advisorSpec -> {
                    advisorSpec.param("chat_memory_conversation_id", inputDTO.getConversationId());
                }).stream();

        Flux<ServerSentEvent<String>> messageEvents = openAiStream.chatResponse()
                .map(data -> ServerSentEvent.<String>builder()
                        .event("chat_output")
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

    @RequestMapping(value = "/reasoner")
    public Flux<ServerSentEvent<String>> reasonerChatApi(
            @Valid ChatInputDTO inputDTO,
            HttpSession session,
            Principal principal
    ) {
        if (StringUtils.hasLength(inputDTO.getConversationId()) && !this.conversationService.existsConversation(inputDTO.getConversationId(), Long.valueOf(principal.getName()))) {
            throw new ServiceException("会话不存在");
        }

        // 为传递conversationId，使用当前session
        if (!StringUtils.hasLength(inputDTO.getConversationId())) {
            String conversationId = (String) session.getAttribute(DEFAULT_AI_CHAT_CONVERSATION);
            if (!StringUtils.hasLength(conversationId)) {
                conversationId = UUIDv7Generator.generateStr();
                session.setAttribute(DEFAULT_AI_CHAT_CONVERSATION, conversationId);
            }
            inputDTO.setConversationId(conversationId);
        }

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(modelName)
                .temperature(this.chatModel.getDefaultOptions().getTemperature())
                .build();

        // ==================== 第一轮推理 ====================
        // 先将 ChatResponse 转换成 ServerSentEvent<String>
        Flux<ServerSentEvent<String>> mappedFirstPhase = this.chatModel.stream(new Prompt(new UserMessage(inputDTO.getContent()), options))
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
                        this.chatModel.stream(new Prompt(List.of(
                                        new AssistantMessage(firstPhaseResult.toString()),
                                        new UserMessage(inputDTO.getContent())
                                ), options))
                                // 转换为 ServerSentEvent 流
                                .map(data -> ServerSentEvent.<String>builder()
                                        .event("message")
                                        .data(data.getResult().getOutput().getText())
                                        .build())
                                // 插入第二轮起始标记和结束标记
                                .startWith(ServerSentEvent.<String>builder()
                                        .event("chat_output")
                                        .data("")
                                        .build())
                                .concatWith(Flux.just(ServerSentEvent.<String>builder()
                                        .event("completed")
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

    /**
     * 获取会话消息
     *
     * @param conversationId 会话ID
     * @param lastMsgId      最后一条消息
     */
    @RequestMapping("{conversationId}/messages")
    public RestData<List<AiChatMessageDTO>> pullAiChatMsgApi(@PathVariable("conversationId") String conversationId, @RequestParam(value = "lastMsgId", required = false) Long lastMsgId, Principal principal) {
        if (!this.conversationService.existsConversation(conversationId, Long.valueOf(principal.getName()))) {
            throw new ServiceException(ServiceErrorCode.RESOURCE_NOT_FOUND);
        }
        return RestData.success(this.conversationService.pullMsg(conversationId, lastMsgId));
    }
}
