package top.mxzero.security.core.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Peng
 * @since 2025/3/18
 */
@Data
public class RefreshTokenDTO {
    @NotBlank
    private String refresh;
}
