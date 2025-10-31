package top.mxzero.filestore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import top.mxzero.oss.enums.AclType;

import java.io.InputStream;

/**
 * @author Peng
 * @since 2025/9/26
 */
@Data
@Builder
@AllArgsConstructor
public class FileUploadRequest {
    private String filename;
    private InputStream inputStream;
    private long size;
    private String contentType;
    private AclType acl;
    private Long userId;
}