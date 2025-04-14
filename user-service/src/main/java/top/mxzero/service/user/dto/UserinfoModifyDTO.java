package top.mxzero.service.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

/**
 * @author Peng
 * @since 2025/3/30
 */
@Data
public class UserinfoModifyDTO {
    @Length(max = 50)
    @NotBlank
    private String nickname;

    @URL
    @Length(max = 255)
    @NotBlank
    private String avatarUrl;

    @JsonIgnore
    private Long id;
}
