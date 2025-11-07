package top.echovoid.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Peng
 * @since 2025/5/13
 */
@Data
public class ProductVariantDTO {
    @NotNull
    @Size(min = 1)
    @Valid
    private List<SpecValueDTO> spec;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;
    private int stock;
    @Length(max = 30)
    private String code;

    @URL
    @Length(max = 255)
    private String imageUrl;
}
