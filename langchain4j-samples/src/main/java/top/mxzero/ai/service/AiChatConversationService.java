package top.mxzero.ai.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.mxzero.ai.entity.AiChatConversation;
import top.mxzero.ai.mapper.AiChatConversationMapper;
import top.mxzero.common.utils.UUIDv7Generator;

import java.util.List;

/**
 * @author Peng
 * @since 2025/5/8
 */
@Service
@AllArgsConstructor
public class AiChatConversationService {
    private final AiChatConversationMapper conversationMapper;


    /**
     * 获取会话列表
     *
     * @param userId      用户ID
     * @param currentPage 当前分页
     * @param pageSize    每页大小
     * @return 会话列表
     */
    public List<AiChatConversation> list(Long userId, long currentPage, long pageSize) {
        Page<AiChatConversation> page = this.conversationMapper.selectPage(new Page<>(currentPage, pageSize), new QueryWrapper<AiChatConversation>().eq("user_id", userId));
        return page.getRecords();
    }

    /**
     * 新建会话
     *
     * @param userId 用户ID
     * @param title  会话标题
     * @return 会话ID
     */
    public String createConversation(Long userId, String title) {
        AiChatConversation conversation = new AiChatConversation();
        conversation.setConversationId(UUIDv7Generator.generateStr());
        conversation.setTitle(title);
        conversation.setUserId(userId);
        this.conversationMapper.insert(conversation);
        return conversation.getConversationId();
    }

    /**
     * 修改会话标题
     *
     * @param conversationId 会话ID
     * @param userId         用户ID
     * @param title          新标题名称
     */
    public void changeConversationTitle(String conversationId, Long userId, String title) {
        UpdateWrapper<AiChatConversation> updateWrapper = new UpdateWrapper<AiChatConversation>()
                .eq("conversation_id", conversationId).eq("user_id", userId)
                .set("title", title);
        this.conversationMapper.update(updateWrapper);
    }

    /**
     * 根据会话ID回去会话信息
     *
     * @param conversationId 会话ID
     * @return 会话信息
     */
    public AiChatConversation getById(String conversationId) {
        return this.conversationMapper.selectById(conversationId);
    }
}
