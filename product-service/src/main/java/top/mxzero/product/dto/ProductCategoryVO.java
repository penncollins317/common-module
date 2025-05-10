package top.mxzero.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ProductCategoryVO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;
    private String path;
    private String imageUrl;
    private Date createdAt;
    private Date updatedAt;
}
