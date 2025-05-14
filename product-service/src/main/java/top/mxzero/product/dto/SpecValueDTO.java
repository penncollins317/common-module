package top.mxzero.product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Peng
 * @since 2025/5/13
 */
@Data
public class SpecValueDTO {
    @NotBlank
    @Length(max = 15)
    private String key;
    @NotBlank
    @Length(max = 30)
    private String value;
}
