package top.mxzero.security.core.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Peng
 * @since 2025/1/21
 */
@Data
public class UsernamePasswordArgs {
    @NotBlank
    @Length(max = 12, min = 2)
    private String username;

    @NotBlank
    @Length(max = 16, min = 8)
    private String password;
}