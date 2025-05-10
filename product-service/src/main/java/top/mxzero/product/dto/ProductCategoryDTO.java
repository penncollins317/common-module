package top.mxzero.product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Data
public class ProductCategoryDTO {
    @NotBlank
    @Length(max = 30)
    private String name;
    @URL
    @Length(max = 255)
    private String imageUrl;
    private Long parentId;
}
