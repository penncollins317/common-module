package top.mxzero.ai.chat;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.mxzero.ai.AiIntetrationAutoConfig;

/**
 * @author Peng
 * @since 2025/3/4
 */
@SpringBootTest(classes = AiIntetrationAutoConfig.class)
public class ChatTest {
    @Autowired
    private ChatModel chatModel;


    @Test
    public void chatTest() {
        ChatResponse response = chatModel.call(
                new Prompt(
                        "Generate the names of 5 famous pirates.",
                        OpenAiChatOptions.builder()
                                .model("deepseek-chat")
                                .temperature(0.4)
                                .build()
                ));
    }
}
