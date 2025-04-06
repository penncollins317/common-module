package top.mxzero.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

/**
 * @author Peng
 * @since 2025/3/18
 */
@Data
public class ChatInputDTO {
    @NotBlank
    private String content;
    private String conversationId;
}
