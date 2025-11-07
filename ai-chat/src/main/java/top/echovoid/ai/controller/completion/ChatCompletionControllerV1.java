package top.echovoid.ai.controller.completion;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import top.echovoid.ai.dto.request.ChatInputDTO;
import top.echovoid.ai.service.AiChatService;
import top.echovoid.common.annotations.AuthenticatedRequired;
import top.echovoid.common.dto.RestData;

import java.security.Principal;

/**
 * 对话模型接口 V1
 *
 * @author Penn Collins
 * @since 2025/4/18
 */
@AuthenticatedRequired
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class ChatCompletionControllerV1 {
    private final AiChatService chatService;

    /**
     * 文字对话
     *
     * @param input 用户输入参数
     */
    @RequestMapping("/chat/completion")
    public Flux<ServerSentEvent<Object>> chatCompletionApi(@Valid ChatInputDTO input, Principal principal) {
        if (principal == null) {
            return this.chatService.chatSteam(input, null);
        }
        return this.chatService.chatSteam(input, Long.valueOf(principal.getName()));
    }

    /**
     * 同步调用对话
     */
    @RequestMapping("/chat/completion/sync")
    public RestData<String> syncChatCompletionApi(@Valid ChatInputDTO input, Principal principal) {
        if (principal == null) {
            return RestData.success(this.chatService.chat(input, null));
        }
        return RestData.success(this.chatService.chat(input, Long.valueOf(principal.getName())));
    }
}
