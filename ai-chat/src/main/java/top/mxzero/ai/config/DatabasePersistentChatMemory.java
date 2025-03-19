package top.mxzero.ai.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.mxzero.ai.entity.AiChatMessage;
import top.mxzero.ai.mapper.AiChatMessageMapper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Peng
 * @since 2024/12/10
 */
@Slf4j
@Component
@AllArgsConstructor
public class DatabasePersistentChatMemory implements ChatMemory {
    private final AiChatMessageMapper messageMapper;

    @Override
    @Transactional
    public void add(String conversationId, List<Message> messages) {
        long currentTimestamp = System.currentTimeMillis();
        messages.forEach(msg -> {
            log.info("conversationId:{}, role:{}:{}", conversationId, msg.getMessageType().name(), msg.getText());
            AiChatMessage assistMessage = AiChatMessage.builder()
                    .conversationId(conversationId)
                    .content(msg.getText())
                    .role(msg.getMessageType().name())
                    .timestamp(currentTimestamp)
                    .createdAt(new Date(currentTimestamp))
                    .build();
            this.messageMapper.insert(assistMessage);
        });
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        QueryWrapper<AiChatMessage> queryWrapper = new QueryWrapper<AiChatMessage>().eq("conversation_id", conversationId).orderByDesc("id");
        List<AiChatMessage> records = this.messageMapper.selectPage(new Page<>(1, lastN), queryWrapper).getRecords();
        return records.stream().map(assistMessage -> {
                    MessageType role = MessageType.valueOf(assistMessage.getRole());
                    return switch (role) {
                        case USER -> new UserMessage(assistMessage.getContent());
                        case ASSISTANT -> new AssistantMessage(assistMessage.getContent());
                        case SYSTEM -> new SystemMessage(assistMessage.getContent());
                        default -> throw new IllegalArgumentException("Unknown role: " + role);
                    };
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void clear(String conversationId) {
        this.messageMapper.delete(new QueryWrapper<AiChatMessage>().eq("conversation_id", conversationId));
    }
}