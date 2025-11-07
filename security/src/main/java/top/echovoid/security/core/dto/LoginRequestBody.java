package top.echovoid.security.core.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Peng
 * @since 2025/2/5
 */
@Data
public class LoginRequestBody {
    /**
     * 账号
     */
    @NotBlank
    @Length(min = 2, max = 20)
    private String username;

    /**
     * 密码
     */
    @NotBlank
    @Length(min = 4, max = 16)
    private String password;

    /**
     * 授权范围
     */
    private String scope;
}
