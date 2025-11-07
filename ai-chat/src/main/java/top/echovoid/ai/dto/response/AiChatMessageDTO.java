package top.echovoid.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Peng
 * @since 2025/3/19
 */
@Data
public class AiChatMessageDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String role;
    private String content;
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long timestamp;
}
