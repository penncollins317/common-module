package top.mxzero.ai.controller.completion;

import jakarta.validation.Valid;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import top.mxzero.ai.dto.request.ChatInputDTO;
import top.mxzero.ai.service.AiConversationService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * 对话模型接口 V1
 *
 * @author Peng
 * @since 2025/4/18
 */
@RestController
@RequestMapping("/api/v1")
public class ChatCompletionControllerV1 {
    private final ChatModel chatModel;
    private final ChatMemory chatMemory;
    private final ChatClient client;
    private final AiConversationService conversationService;

    public ChatCompletionControllerV1(ChatModel chatModel, ChatMemory chatMemory, AiConversationService conversationService) {
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
     * 文字对话
     *
     * @param input 用户输入参数
     */
    @RequestMapping("/chat/completion")
    public Flux<ServerSentEvent<Object>> chatCompletionApi(@Valid ChatInputDTO input, Principal principal) {
        OpenAiChatOptions.Builder optionsBuilder = OpenAiChatOptions.builder()
                .temperature(input.isThinkingEnabled() ? Double.valueOf(0.3) : this.chatModel.getDefaultOptions().getTemperature());

        if (StringUtils.hasLength(input.getModel())) {
            optionsBuilder.model(input.getModel());
        } else {
            optionsBuilder.model(this.chatModel.getDefaultOptions().getModel());
        }
        Prompt prompt = new Prompt(input.getContent(), optionsBuilder.build());

        return this.client.prompt(prompt)
                .advisors(advisorSpec -> {
                    advisorSpec.param("chat_memory_conversation_id", input.getConversationId());
                })
                .stream()
                .chatResponse()
                .map(data -> {
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("v", data.getResult().getOutput().getText());
                    return ServerSentEvent.builder()
                            .event("message")
                            .data(responseData)
                            .build();
                });
    }
}
