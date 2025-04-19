package top.mxzero.ai.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import top.mxzero.ai.dto.request.CreateConversationDTO;
import top.mxzero.ai.dto.response.AiChatMessageDTO;
import top.mxzero.ai.dto.response.ConversationDTO;
import top.mxzero.ai.service.AiChatService;
import top.mxzero.ai.service.AiConversationService;
import top.mxzero.common.annotations.AuthenticatedRequired;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.params.PageParam;

import java.security.Principal;
import java.util.List;

/**
 * AI会话接口
 *
 * @author Peng
 * @since 2025/3/18
 */
@AuthenticatedRequired
@RestController
@AllArgsConstructor
@RequestMapping("/ai/conversations")
public class AiConversationController {
    private final AiConversationService conversationService;
    private final AiChatService aiChatService;

    /**
     * 会话列表
     *
     * @param param 分页参数
     */
    @GetMapping
    public RestData<List<ConversationDTO>> listConversationApi(
            PageParam param,
            Principal principal
    ) {
        return RestData.success(this.aiChatService.listConversations(Long.valueOf(principal.getName()), param));
    }

    /**
     * 创建会话
     *
     * @param dto 会话数据
     */
    @PostMapping
    public RestData<ConversationDTO> createConversationApi(@Valid @RequestBody CreateConversationDTO dto, Principal principal) {
        dto.setUserId(Long.valueOf(principal.getName()));
        return RestData.success(this.aiChatService.createConversation(Long.valueOf(principal.getName()), dto.getTitle()));
    }

    /**
     * 修改会话信息
     *
     * @param conversationId 会话ID
     * @param dto            会话数据
     */
    @PutMapping("{conversationId}")
    public RestData<Boolean> updateConversationTitleApi(@PathVariable("conversationId") String conversationId, @Valid @RequestBody CreateConversationDTO dto, Principal principal) {
        dto.setUserId(Long.valueOf(principal.getName()));
        return RestData.success(this.aiChatService.renameConversation(conversationId, Long.valueOf(principal.getName()), dto.getTitle()));
    }

    /**
     * 删除会话
     *
     * @param conversationId 会话ID
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{conversationId}")
    public void deleteConversationApi(@PathVariable("conversationId") String conversationId, Principal principal) {
        this.aiChatService.deleteConversation(conversationId, Long.valueOf(principal.getName()));
    }


    /**
     * 获取会话历史消息
     *
     * @param conversationId 会话ID
     */
    @RequestMapping("{conversationId}/msg")
    public RestData<List<AiChatMessageDTO>> conversationMessageListApi(
            @PathVariable("conversationId") String conversationId,
            Principal principal,
            PageParam param
    ) {
        return RestData.success(this.aiChatService.getConversationMessages(conversationId, Long.valueOf(principal.getName()), param));
    }
}
