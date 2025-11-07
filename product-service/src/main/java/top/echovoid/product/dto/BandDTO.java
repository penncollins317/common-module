package top.echovoid.product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

/**
 * @author Penn Collins
 * @since 2025/5/11
 */
@Data
public class BandDTO {
    @NotBlank
    @Length(max = 30)
    private String name;
    @URL
    @Length(max = 255)
    private String imageUrl;
}
