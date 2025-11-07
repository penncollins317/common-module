package top.echovoid.product.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @author Penn Collins
 * @since 2025/5/10
 */
@Data
public class ProductCategory {
    private Long id;
    private String name;
    private Long parentId;
    private String path;
    private String imageUrl;
    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;
}