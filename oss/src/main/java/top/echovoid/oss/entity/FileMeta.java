package top.echovoid.oss.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.echovoid.oss.enums.AclType;
import top.echovoid.oss.enums.FileStatus;

import java.util.Date;

/**
 * @author Peng
 * @since 2024/10/19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("file_meta")
public class FileMeta {
    private Long id;
    private String name;
    private String storePath;
    private String accessUrl;
    private String contentType;
    private Long size;
    private FileStatus status;
    private String md5;
    private String sha256;
    private Long userId;
    private AclType acl;
    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;
}