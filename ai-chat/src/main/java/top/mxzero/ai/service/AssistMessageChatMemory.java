package top.mxzero.ai.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.mxzero.ai.entity.AssistMessage;
import top.mxzero.ai.repository.AssistMessageRepository;

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
public class AssistMessageChatMemory implements ChatMemory {

    private final AssistMessageRepository messageRepository;

    @Override
    @Transactional
    public void add(String conversationId, List<Message> messages) {
        // 批量插入消息
        messages.forEach(msg -> {
            log.info("conversationId:{}, role:{}:{}", conversationId, msg.getMessageType().name(), msg.getText());
            AssistMessage assistMessage = AssistMessage.builder()
                    .conversationId(conversationId)
                    .content(msg.getText())
                    .role(msg.getMessageType().name())
                    .createdAt(new Date())
                    .build();
            this.messageRepository.save(assistMessage);
        });
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        log.info("conversationId:{}", lastN);
        Pageable pageable = PageRequest.ofSize(lastN);
        Page<AssistMessage> messagePage = messageRepository.findByConversationId(conversationId, pageable);
        return messagePage.getContent().stream().map(assistMessage -> {
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
        this.messageRepository.deleteAll();
    }
}