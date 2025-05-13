package top.mxzero.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * @author Peng
 * @since 2025/5/13
 */
@Data
public class ProductSpecDTO {
    @NotBlank
    @Length(max = 15)
    private String key;
    @NotNull
    @Size(min = 1)
    private List<String> values;
}
