package top.mxzero.ai.service;


import jakarta.annotation.Nullable;
import top.mxzero.ai.dto.request.CreateConversationDTO;
import top.mxzero.ai.dto.response.ConversationDTO;

import java.util.List;

/**
 * @author Peng
 * @since 2025/3/18
 */
public interface AiConversationService {

    /**
     * 创建会话
     *
     * @param dto 会话数据
     * @return 会话ID
     */
    String create(CreateConversationDTO dto);

    /**
     * 获取会话
     *
     * @param userId             用户ID
     * @param lastConversationId 上一条会话ID
     * @return 会话列表
     */
    List<ConversationDTO> listConversation(Long userId, @Nullable String lastConversationId);

    /**
     * 删除会话
     *
     * @param conversationId 会话ID
     * @param userId         用户ID
     */
    boolean deleteConversation(String conversationId, Long userId);

    /**
     * 判断会话是否存在
     *
     * @param conversationId 会话ID
     * @param userId         用户ID
     */
    boolean existsConversation(String conversationId, Long userId);
}
