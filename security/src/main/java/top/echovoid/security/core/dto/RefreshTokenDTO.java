package top.echovoid.security.core.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Penn Collins
 * @since 2025/3/18
 */
@Data
public class RefreshTokenDTO {
    @NotBlank
    private String refresh;
}
