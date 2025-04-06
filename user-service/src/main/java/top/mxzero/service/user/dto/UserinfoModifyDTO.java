package top.mxzero.service.user.dto;

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
    private String nickname;
    @URL
    @Length(max = 255)
    private String avatarUrl;

    private Long id;
}
