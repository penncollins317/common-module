package top.echovoid.filestore.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Penn Collins
 * @since 2025/10/30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileShared {
    private Long id;
    private Long fileId;
    private Long userId;
    private String token;
    private String url;
    private LocalDateTime expireAt;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}

