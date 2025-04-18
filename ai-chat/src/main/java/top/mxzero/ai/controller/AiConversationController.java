package top.mxzero.ai.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import top.mxzero.ai.dto.request.CreateConversationDTO;
import top.mxzero.ai.dto.response.AiChatMessageDTO;
import top.mxzero.ai.dto.response.ConversationDTO;
import top.mxzero.ai.service.AiConversationService;
import top.mxzero.common.annotations.AuthenticatedRequired;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.exceptions.ServiceErrorCode;
import top.mxzero.common.exceptions.ServiceException;

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

    /**
     * 会话列表
     *
     * @param lastConversationId 上一条会话ID
     */
    @GetMapping
    public RestData<List<ConversationDTO>> listConversationApi(
            @RequestParam(value = "last", required = false) String lastConversationId,
            Principal principal
    ) {
        return RestData.success(this.conversationService.listConversation(Long.valueOf(principal.getName()), lastConversationId));
    }

    /**
     * 创建会话
     *
     * @param dto 会话数据
     */
    @PostMapping
    public RestData<String> createConversationApi(@Valid @RequestBody CreateConversationDTO dto, Principal principal) {
        dto.setUserId(Long.valueOf(principal.getName()));
        return RestData.success(this.conversationService.create(dto));
    }

    /**
     * 删除会话
     *
     * @param conversationId 会话ID
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{conversationId}")
    public void deleteConversationApi(@PathVariable("conversationId") String conversationId, Principal principal) {
        boolean result = this.conversationService.deleteConversation(conversationId, Long.valueOf(principal.getName()));
        if (!result) {
            throw new ServiceException(ServiceErrorCode.RESOURCE_NOT_FOUND);
        }
    }


    /**
     * 获取会话历史消息
     * @param conversationId 会话ID
     */
    @RequestMapping("{conversationId}/msg")
    public RestData<List<AiChatMessageDTO>> conversationMessageListApi(@PathVariable("conversationId") String conversationId){
        return RestData.success(this.conversationService.pullMsg(conversationId, null));
    }
}
