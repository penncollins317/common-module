package top.mxzero.ai.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Peng
 * @since 2024/12/10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AssistMessage {
    @Id
    @GeneratedValue
    private Long id;
    private String conversationId;
    private String content;
    private String role;
    private Date createdAt;
}