package top.mxzero.product.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author Peng
 * @since 2025/5/11
 */
@Data
public class Band {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String name;
    private String imageUrl;
}
