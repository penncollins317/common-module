package top.echovoid.security.apikeys.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import top.echovoid.security.apikeys.enums.OutAppStatus;

import java.util.Date;

/**
 * @author Peng
 * @since 2025/2/15
 */
@Data
public class OutAppDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String name;
    private String iconUrl;
    private String description;
    private String devServerUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    private OutAppStatus status;
    private Date createdAt;
    private Date updatedAt;
}
