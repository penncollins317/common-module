package top.mxzero.ai.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.mxzero.ai.entity.AiChatConversation;
import top.mxzero.ai.service.AiChatConversationService;
import top.mxzero.common.annotations.AuthenticatedRequired;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.params.PageParam;

import java.security.Principal;
import java.util.List;

/**
 * AI聊天会话接口
 *
 * @author Peng
 * @since 2025/5/8
 */
@RestController
@RequestMapping("/v1")
@AuthenticatedRequired
@AllArgsConstructor
public class AiChatConversationController {
    private final AiChatConversationService conversationService;


    /**
     * 新建会话
     *
     * @param title 会话标题
     */
    @PostMapping("/ai/conversations")
    public RestData<String> createConversationApi(Principal principal, String title) {
        if (title != null && title.length() > 20) {
            title = title.trim().substring(0, 20);
        }
        return RestData.success(this.conversationService.createConversation(Long.valueOf(principal.getName()), title));
    }

    /**
     * 会话列表
     *
     * @param pageParam 分页信息
     */
    @GetMapping("/ai/conversations")
    public RestData<List<AiChatConversation>> listConversationApi(Principal principal, PageParam pageParam) {
        return RestData.success(this.conversationService.list(Long.valueOf(principal.getName()), pageParam.getPage(), pageParam.getSize()));
    }

    /**
     * 修改会话标题
     *
     * @param conversationId 会话ID
     * @param title          新的标题
     */
    @PutMapping("/api/conversations/{conversation_id}")
    public RestData<?> changeConversationApi(
            @PathVariable("conversation_id") String conversationId,
            Principal principal, @RequestParam("title") String title
    ) {
        this.conversationService.changeConversationTitle(conversationId, Long.valueOf(principal.getName()), title);
        return RestData.success();
    }
}
