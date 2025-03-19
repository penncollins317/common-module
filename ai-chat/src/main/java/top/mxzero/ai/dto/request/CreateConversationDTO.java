package top.mxzero.ai.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Peng
 * @since 2025/3/18
 */
@Data
public class CreateConversationDTO {
    @NotBlank
    @Length(min = 1, max = 50)
    private String title;

    @JsonIgnore
    private Long userId;
}