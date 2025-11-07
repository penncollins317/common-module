package top.echovoid.security.oauth2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Penn Collins
 * @since 2025/4/12
 */
@Data
public class RegisterDTO {
    @NotBlank
    @Length(max = 20)
    private String username;
    @Email
    @NotBlank
    @Length(max = 100)
    private String email;
    @Length(min = 6, max = 30)
    private String password;
}
