package top.mxzero.ai.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import top.mxzero.ai.entity.AssistMessage;

/**
 * @author Peng
 * @since 2025/2/20
 */
public interface AssistMessageRepository extends JpaRepository<AssistMessage, Long> {
    int deleteByConversationId(String conversationId);

    Page<AssistMessage> findByConversationId(String conversationId, Pageable pageable);
}