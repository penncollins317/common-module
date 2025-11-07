package top.echovoid.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Peng
 * @since 2025/3/18
 */
@Data
public class ChatInputDTO {
    @NotBlank
    private String content;
    @NotBlank
    private String conversationId;

    private String model;
    private boolean thinkingEnabled;
    private boolean searchEnabled;
}
