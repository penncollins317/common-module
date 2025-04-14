package top.mxzero.oss.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import top.mxzero.oss.enums.FileStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Peng
 * @since 2024/10/19
 */
@Data
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
    private Date createdAt;
    private Date updatedAt;
}