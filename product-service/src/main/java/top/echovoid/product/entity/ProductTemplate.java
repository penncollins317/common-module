package top.echovoid.product.entity;

import lombok.Data;

/**
 * @author Penn Collins
 * @since 2025/5/11
 */
@Data
public class ProductTemplate {
    private Long id;
    private String name;
    private String mainImageUrl;
    private Long categoryId;
    private Long bandId;

}
