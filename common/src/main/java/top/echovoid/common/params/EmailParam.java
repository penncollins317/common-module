package top.echovoid.common.params;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @author Peng
 * @email penncollins317@gmail.com
 * @since 2024/3/12
 */
@Data
public class EmailParam implements Serializable {
    @Email
    @NotBlank
    @Length(max = 100)
    private String email;

    @NotBlank(message = "业务代码为空")
    private String serviceCode;

    @NotBlank(message = "token为空")
    private String token;
}
