package top.echovoid.ai.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.echovoid.ai.entity.AiChatConversation;
import top.echovoid.ai.service.AiChatConversationService;
import top.echovoid.common.annotations.AuthenticatedRequired;
import top.echovoid.common.dto.RestData;
import top.echovoid.common.params.PageParam;

import java.security.Principal;
import java.util.List;

/**
 * AI聊天会话接口
 *
 * @author Penn Collins
 * @since 2025/5/8
 */
@Tag(name = "AI 聊天会话接口", description = "提供 AI 对话会话的新建、查询列表及标题修改功能")
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
    @Operation(summary = "新建会话", description = "为当前用户创建一个新的 AI 聊天会话")
    @PostMapping("/ai/conversations")
    public RestData<String> createConversationApi(@Parameter(hidden = true) Principal principal,
            @Parameter(description = "会话标题") String title) {
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
    @Operation(summary = "获取会话列表", description = "分页查询当前用户的所有 AI 聊天会话记录")
    @GetMapping("/ai/conversations")
    public RestData<List<AiChatConversation>> listConversationApi(@Parameter(hidden = true) Principal principal,
            PageParam pageParam) {
        return RestData.success(this.conversationService.list(Long.valueOf(principal.getName()), pageParam.getPage(),
                pageParam.getSize()));
    }

    /**
     * 修改会话标题
     *
     * @param conversationId 会话ID
     * @param title          新的标题
     */
    @Operation(summary = "修改会话标题", description = "更新指定 AI 聊天会话的显示名称")
    @PutMapping("/api/conversations/{conversation_id}")
    public RestData<?> changeConversationApi(
            @Parameter(description = "会话唯一标识ID") @PathVariable("conversation_id") String conversationId,
            @Parameter(hidden = true) Principal principal,
            @Parameter(description = "新的会话标题") @RequestParam("title") String title) {
        this.conversationService.changeConversationTitle(conversationId, Long.valueOf(principal.getName()), title);
        return RestData.success();
    }
}
