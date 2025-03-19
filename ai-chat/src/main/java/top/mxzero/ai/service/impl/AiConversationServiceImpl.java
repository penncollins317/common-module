package top.mxzero.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import top.mxzero.ai.dto.request.CreateConversationDTO;
import top.mxzero.ai.dto.response.ConversationDTO;
import top.mxzero.ai.entity.AiConversation;
import top.mxzero.ai.mapper.AiConversationMapper;
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

    @Override
    @Transactional
    public String create(CreateConversationDTO dto) {
        String conversationId = UUIDv7Generator.generate().toString();
        AiConversation conversation = new AiConversation();
        conversation.setConversationId(UUIDv7Generator.generate().toString());
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
        Page<AiConversation> page = this.conversationMapper.selectPage(new Page<>(1, 10), queryWrapper);
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
}
