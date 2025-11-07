package top.echovoid.security.oauth2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Peng
 * @since 2025/4/5
 */
@Data
public class OAuthClientDTO {
    @NotBlank
    @Length(min = 2, max = 20)
    private String name;
    @NotBlank
    private String callbackUrls; // 分号分隔多个回调地址
    @NotBlank
    private String description;
    @NotNull
    private MultipartFile logoPicture; // 文件上传后的存储路径
}

