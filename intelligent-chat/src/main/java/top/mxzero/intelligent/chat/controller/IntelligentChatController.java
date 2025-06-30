package top.mxzero.intelligent.chat.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import top.mxzero.common.annotations.AuthenticatedRequired;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.exceptions.ServiceException;
import top.mxzero.common.utils.JsonUtils;
import top.mxzero.intelligent.chat.entity.ChatConversation;
import top.mxzero.intelligent.chat.service.ChatConversationService;

import java.security.Principal;
import java.util.Map;

/**
 * 聊天接口
 *
 * @author Peng
 * @since 2025/6/27
 */
@AuthenticatedRequired
@RestController
public class IntelligentChatController {
    private final ChatClient chatClient;
    private final ChatConversationService chatConversationService;

    public IntelligentChatController(ChatModel chatModel, ChatConversationService chatConversationService) {
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(100)
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();
        chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                ).build();
        this.chatConversationService = chatConversationService;
    }

    /**
     * 聊天接口
     *
     * @param text           用户输入词
     * @param conversationId 会话ID
     */
    @GetMapping(value = "/intelligent/chat")
    public RestData<String> chatApi(
            @RequestParam("text") String text,
            @RequestParam("conversation_id") String conversationId,
            Principal principal
    ) {
        ChatConversation chatConversation = this.chatConversationService.get(conversationId);
        if (chatConversation == null || !chatConversation.getUserId().equals(Long.valueOf(principal.getName()))) {
            throw new ServiceException("会话不存在");
        }
        Prompt prompt = Prompt.builder()
                .messages(new UserMessage(text))
                .build();
        String content = chatClient.prompt(prompt)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call().content();
        return RestData.ok(content);
    }

    /**
     * 聊天流式响应接口
     *
     * @param text           用户输入词
     * @param conversationId 会话ID
     */
    @GetMapping(value = "/intelligent/chat/stream")
    public Flux<ServerSentEvent<String>> chatStreamApi(
            @RequestParam("text") String text,
            @RequestParam("conversation_id") String conversationId,
            Principal principal
    ) {
        ChatConversation chatConversation = this.chatConversationService.get(conversationId);
        if (chatConversation == null || !chatConversation.getUserId().equals(Long.valueOf(principal.getName()))) {
           throw new ServiceException("会话不存在");
        }
        Prompt prompt = Prompt.builder()
                .messages(new UserMessage(text))
                .build();
        return chatClient.prompt(prompt).stream().chatResponse()
                .filter(data -> StringUtils.hasLength(data.getResult().getOutput().getText()))
                .map(data -> ServerSentEvent.<String>builder()
                        .event("message")
                        .data(
                                JsonUtils.stringify(
                                        Map.of("v", data.getResult().getOutput().getText())
                                )
                        )
                        .build()
                );
    }
}
