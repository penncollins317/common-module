package top.echovoid.common.params;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import top.echovoid.common.utils.PhoneUtil;

import java.io.Serializable;

/**
 * @author Penn Collins
 * @email penncollins317@gmail.com
 * @since 2023/9/10
 */
@Data
public class PhoneParam implements Serializable {
    @NotBlank(message = "手机号码为空")
    @Pattern(regexp = PhoneUtil.PHONE_REGEX, message = "手机号码无效")
    private String phone;

    @NotBlank(message = "业务码为空")
    private String serviceCode;
    
    private String token;
}