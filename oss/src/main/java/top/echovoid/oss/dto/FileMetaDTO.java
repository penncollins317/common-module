package top.echovoid.oss.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.echovoid.oss.enums.AclType;
import top.echovoid.oss.enums.FileStatus;

import java.util.Date;

/**
 * @author Peng
 * @since 2025/4/14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileMetaDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String name;
    private String storePath;
    private String accessUrl;
    private String contentType;
    private Long size;
    private FileStatus status;
    private String md5;
    private String sha256;
    private AclType acl;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    private Date createdAt;
    private Date updatedAt;
}
