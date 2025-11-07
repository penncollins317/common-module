package top.echovoid.security.apikeys.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

/**
 * @author Peng
 * @since 2025/2/15
 */
@Data
public class CreateOutAppRequestDTO {
    @NotBlank
    @Length(min = 2, max = 20)
    private String name;

    @Length(max = 255)
    @NotBlank
    @URL
    private String iconUrl;

    @Length(max = 255)
    @NotBlank
    private String description;

    @JsonIgnore
    private Long userId;
}