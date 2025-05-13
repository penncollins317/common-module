package top.mxzero.product.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import java.util.List;

/**
 * @author Peng
 * @since 2025/5/13
 */
@Data
public class ProductDTO {
    @NotBlank
    @Length(max = 50)
    private String name;
    @Length(max = 255)
    private String subTitle;
    @URL
    @NotBlank
    @Length(max = 255)
    private String imageUrl;
    @NotNull
    private Long categoryId;
    private List<String> subImageUrls;
    private List<String> detailImageUrls;
    private String description;
    private List<String> tags;
    private Boolean isMultiSpec;
    @NotNull
    @Size(min = 1)
    @Valid
    private List<ProductSpecDTO> specs;
    @NotNull
    @Size(min = 1)
    @Valid
    private List<ProductVariantDTO> skus;
}