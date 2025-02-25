package top.mxzero.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;

/**
 * @author Peng
 * @since 2024/11/27
 */
@Controller
public class ChatController {
    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder, ChatMemory chatMemory) {
        this.chatClient = builder.defaultAdvisors(new PromptChatMemoryAdvisor(chatMemory)).build();
    }


    @ResponseBody
    @GetMapping("/chat/text")
    public String chat(String input) {
        return this.chatClient.prompt()
                .user(input)
                .call()
                .content();
    }

    @RequestMapping("/chat")
    public String completion() {
        return "chat.html";
    }
}
