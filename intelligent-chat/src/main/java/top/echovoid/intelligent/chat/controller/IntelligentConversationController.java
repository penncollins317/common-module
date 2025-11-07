package top.echovoid.intelligent.chat.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.echovoid.common.annotations.AuthenticatedRequired;
import top.echovoid.common.dto.RestData;
import top.echovoid.intelligent.chat.entity.ChatConversation;
import top.echovoid.intelligent.chat.service.ChatConversationService;

import java.security.Principal;
import java.util.List;

/**
 * 聊天会话管理
 *
 * @author Penn Collins
 * @since 2025/6/30
 */
@AuthenticatedRequired
@RestController
@RequestMapping("/intelligent/conversations")
@AllArgsConstructor
public class IntelligentConversationController {
    private final ChatConversationService chatConversationService;

    /**
     * 新建会话
     */
    @PostMapping
    public RestData<String> createConversationApi(Principal principal) {
        return RestData.ok(this.chatConversationService.create(Long.valueOf(principal.getName())));
    }

    /**
     * 会话列表
     */
    @GetMapping
    public RestData<List<ChatConversation>> listConversationApi(Principal principal) {
        return RestData.ok(this.chatConversationService.listByUserId(Long.valueOf(principal.getName())));
    }

    /**
     * 删除会话
     *
     * @param conversationId 会话ID
     */
    @DeleteMapping("{conversation_id}")
    public RestData<Boolean> deleteConversation(@PathVariable("conversation_id") String conversationId, Principal principal) {
        return RestData.ok(this.chatConversationService.remove(conversationId, Long.valueOf(principal.getName())));
    }

}
