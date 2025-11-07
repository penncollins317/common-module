package top.echovoid.oss.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @author Peng
 * @since 2024/10/20
 */
@Data
public class PrepareUploadParam implements Serializable {
    @NotBlank
    @Length(max = 255)
    private String name;

    private String contentType;
    @NotBlank
    @Length(min = 32, max = 64)
    private String hash;
    @NotNull
    private Long size;
}
