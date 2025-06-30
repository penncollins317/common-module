package top.mxzero.intelligent.chat.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.utils.JsonUtils;

import java.util.ArrayList;
import java.util.Map;

/**
 * 文字接口
 *
 * @author Peng
 * @since 2025/6/27
 */
@RestController
public class IntelligentTextController {

    private static final String SYSTEM_PROMPT = "你的名字叫小星，是一个智能陪伴助手，你可以简单回答用户提的问题，或者与用户聊天沟通，要有人情味";
    private final ChatClient chatClient;

    public IntelligentTextController(ChatModel chatModel) {
        chatClient = ChatClient.builder(chatModel).build();
    }


    /**
     * 文字生成接口
     *
     * @param text       用户输入内容
     * @param systemText 系统提示词
     */
    @GetMapping(value = "/intelligent/text")
    public RestData<String> textGeneApi(
            @RequestParam("text") String text,
            @RequestParam(value = "system_text", required = false) String systemText
    ) {
        ArrayList<Message> messages = new ArrayList<>(2);
        messages.add(new SystemMessage(StringUtils.hasText(systemText) ? systemText : SYSTEM_PROMPT));
        messages.add(new UserMessage(text));
        Prompt prompt = Prompt.builder()
                .messages(messages)
                .build();
        return RestData.ok(chatClient.prompt(
                prompt
        ).call().content());
    }

    @GetMapping(value = "/intelligent/text/stream")
    public Flux<ServerSentEvent<String>> textGeneSteamApi(
            @RequestParam("text") String text,
            @RequestParam(value = "system_text", required = false) String systemText
    ) {
        ArrayList<Message> messages = new ArrayList<>(2);
        messages.add(new SystemMessage(StringUtils.hasText(systemText) ? systemText : SYSTEM_PROMPT));
        messages.add(new UserMessage(text));
        Prompt prompt = Prompt.builder()
                .messages(messages)
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
