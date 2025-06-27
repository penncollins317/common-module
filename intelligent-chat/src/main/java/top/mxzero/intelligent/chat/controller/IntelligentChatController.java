package top.mxzero.intelligent.chat.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Peng
 * @since 2025/6/27
 */
@RestController
public class IntelligentChatController {
    private final ChatClient chatClient;

    public IntelligentChatController(ChatModel chatModel) {
        chatClient = ChatClient.builder(chatModel).build();
    }

    @GetMapping(value = "/intelligent/chat")
    public String chatApi(@RequestParam("text") String text) {
        Prompt prompt = Prompt.builder()
                .messages(
                        new SystemMessage("你是一个智能陪伴助手，你可以简单回答用户提的问题，或者与用户聊天沟通，要有人情味"),
                        new UserMessage(text)
                )
                .build();
        return chatClient.prompt(
                prompt
        ).call().content();
    }

}
