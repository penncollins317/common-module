package top.mxzero.security.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Peng
 * @since 2025/1/21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserinfoDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String username;
    private String nickname;
    private String avatarUrl;
    private Long version;
}