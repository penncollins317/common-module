package top.echovoid.intelligent.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.echovoid.intelligent.chat.entity.ChatConversation;
import top.echovoid.intelligent.chat.mapper.ChatConversationMapper;
import top.echovoid.intelligent.chat.service.ChatConversationService;

import java.util.List;

/**
 * @author Peng
 * @since 2025/6/30
 */
@Service
@AllArgsConstructor
public class ChatConversationServiceImpl implements ChatConversationService {
    private final ChatConversationMapper chatConversationMapper;

    @Override
    public ChatConversation get(String conversationId) {
        return this.chatConversationMapper.selectById(conversationId);
    }

    @Override
    public List<ChatConversation> listByUserId(Long userId) {
        QueryWrapper<ChatConversation> queryWrapper = new QueryWrapper<ChatConversation>().eq("user_id", userId).orderByDesc("updated_at");
        return chatConversationMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public String create(Long userId) {
        ChatConversation chatConversation = new ChatConversation();
        chatConversation.setUserId(userId);
        chatConversation.setName("新会话");
        this.chatConversationMapper.insert(chatConversation);
        return chatConversation.getConversationId();
    }

    @Override
    @Transactional
    public boolean updateName(String conversationId, String name) {
        UpdateWrapper<ChatConversation> updateWrapper = new UpdateWrapper<ChatConversation>().eq("conversation_id", conversationId).set("name", name);
        return this.chatConversationMapper.update(updateWrapper) > 0;
    }

    @Override
    @Transactional
    public boolean remove(String conversationId, Long userId) {
        ChatConversation conversation = this.chatConversationMapper.selectById(conversationId);
        if (conversation == null || !conversation.getUserId().equals(userId)) {
            return false;
        }
        return this.chatConversationMapper.deleteById(conversationId) > 0;
    }
}
