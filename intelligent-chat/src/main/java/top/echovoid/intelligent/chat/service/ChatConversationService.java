package top.echovoid.intelligent.chat.service;

import top.echovoid.intelligent.chat.entity.ChatConversation;

import java.util.List;

/**
 * @author Penn Collins
 * @since 2025/6/30
 */
public interface ChatConversationService {
    /**
     * 获取会话
     *
     * @param conversationId 会话ID
     */
    ChatConversation get(String conversationId);

    /**
     * 获取用户的会话列表
     *
     * @param userId 用户ID
     */
    List<ChatConversation> listByUserId(Long userId);

    /**
     * 创建会话
     *
     * @param userId 用户ID
     * @return 会话ID
     */
    String create(Long userId);

    /**
     * 修改会话名称
     *
     * @param conversationId 会话ID
     * @param name           新的会话名称
     */
    boolean updateName(String conversationId, String name);

    /**
     * 删除会话
     *
     * @param conversationId 会话ID
     * @param userId         用户ID
     */
    boolean remove(String conversationId, Long userId);
}
