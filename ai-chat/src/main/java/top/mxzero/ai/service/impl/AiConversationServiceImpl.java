package top.mxzero.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import top.mxzero.ai.dto.request.CreateConversationDTO;
import top.mxzero.ai.dto.response.AiChatMessageDTO;
import top.mxzero.ai.dto.response.ConversationDTO;
import top.mxzero.chat.entity.AiChatMessage;
import top.mxzero.chat.entity.AiConversation;
import top.mxzero.chat.mapper.AiChatMessageMapper;
import top.mxzero.chat.mapper.AiConversationMapper;
import top.mxzero.ai.service.AiConversationService;
import top.mxzero.common.utils.DeepBeanUtil;
import top.mxzero.common.utils.UUIDv7Generator;

import java.util.List;

/**
 * @author Peng
 * @since 2025/3/18
 */
@Service
@AllArgsConstructor
public class AiConversationServiceImpl implements AiConversationService {
    private final AiConversationMapper conversationMapper;
    private final AiChatMessageMapper chatMessageMapper;

    @Override
    @Transactional
    public String create(CreateConversationDTO dto) {
        String conversationId = UUIDv7Generator.generate().toString();
        AiConversation conversation = new AiConversation();
        conversation.setConversationId(conversationId);
        conversation.setTitle(dto.getTitle());
        conversation.setUserId(dto.getUserId());
        this.conversationMapper.insert(conversation);
        return conversationId;
    }

    @Override
    public List<ConversationDTO> listConversation(Long userId, @Nullable String lastConversationId) {
        QueryWrapper<AiConversation> queryWrapper = new QueryWrapper<AiConversation>().eq("user_id", userId).orderByDesc("conversation_id");
        if (StringUtils.hasLength(lastConversationId)) {
            queryWrapper.lt("conversation_id", lastConversationId);
        }
        Page<AiConversation> page = this.conversationMapper.selectPage(new Page<>(1, 30), queryWrapper);
        return DeepBeanUtil.copyProperties(page.getRecords(), ConversationDTO::new);
    }

    @Override
    public boolean deleteConversation(String conversationId, Long userId) {
        return this.conversationMapper.delete(new QueryWrapper<AiConversation>().eq("conversation_id", conversationId).eq("user_id", userId)) > 0;
    }

    @Override
    public boolean existsConversation(String conversationId, Long userId) {
        return this.conversationMapper.exists(new QueryWrapper<AiConversation>().eq("conversation_id", conversationId).eq("user_id", userId));
    }

    @Override
    public long msgCnt(String conversationId) {
        return this.chatMessageMapper.selectCount(new QueryWrapper<AiChatMessage>().eq("conversation_id", conversationId));
    }

    @Override
    public List<AiChatMessageDTO> pullMsg(String conversationId, @Nullable Long lastMsgId) {
        QueryWrapper<AiChatMessage> queryWrapper = new QueryWrapper<AiChatMessage>().eq("conversation_id", conversationId).orderByDesc("id");
        if (lastMsgId != null) {
            queryWrapper.lt("id", lastMsgId);
        }
        IPage<AiChatMessage> page = this.chatMessageMapper.selectPage(new Page<>(1, 10), queryWrapper);
        return page.getRecords().stream().map(msg -> DeepBeanUtil.copyProperties(msg, AiChatMessageDTO::new)).toList();
    }

    @Override
    public ConversationDTO getById(String conversationId) {
        AiConversation aiConversation = this.conversationMapper.selectById(conversationId);
        return DeepBeanUtil.copyProperties(aiConversation, ConversationDTO.class);
    }
}
