package top.mxzero.intelligent.chat.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.mxzero.common.annotations.AuthenticatedRequired;
import top.mxzero.common.dto.RestData;

/**
 * 文字接口
 *
 * @author Peng
 * @since 2025/6/27
 */
@AuthenticatedRequired
@RestController
public class IntelligentChatController {

    private static final String SYSTEM_PROMPT = "你的名字叫小星，是一个智能陪伴助手，你可以简单回答用户提的问题，或者与用户聊天沟通，要有人情味";
    private final ChatClient chatClient;

    public IntelligentChatController(ChatModel chatModel) {
        chatClient = ChatClient.builder(chatModel).build();
    }

    /**
     * 文字生成
     *
     * @param text 用户输入词
     */
    @GetMapping(value = "/intelligent/chat")
    public RestData<String> chatApi(@RequestParam("text") String text) {
        Prompt prompt = Prompt.builder()
                .messages(
                        new SystemMessage(SYSTEM_PROMPT),
                        new UserMessage(text)
                )
                .build();
        return RestData.ok(chatClient.prompt(
                prompt
        ).call().content());
    }

}
