package top.echovoid.ai.service;


import jakarta.annotation.Nullable;
import top.echovoid.ai.dto.request.CreateConversationDTO;
import top.echovoid.ai.dto.response.AiChatMessageDTO;
import top.echovoid.ai.dto.response.ConversationDTO;

import java.util.List;

/**
 * @author Penn Collins
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
     * @param userId         用户名
     */
    boolean deleteConversation(String conversationId, Long userId);

    /**
     * 判断会话是否存在
     *
     * @param conversationId 会话ID
     * @param userId         用户名
     */
    boolean existsConversation(String conversationId, Long userId);

    /**
     * 获取会话消息数量
     *
     * @param conversationId 会话ID
     */
    long msgCnt(String conversationId);

    /**
     * 获取消息
     *
     * @param conversationId 会话ID
     * @param lastMsgId      上一条消息ID
     * @return Ai消息列表
     */
    List<AiChatMessageDTO> pullMsg(String conversationId, @Nullable Long lastMsgId);

    /**
     * 获取会话信息
     *
     * @param conversationId 会话ID
     */
    ConversationDTO getById(String conversationId);
}
