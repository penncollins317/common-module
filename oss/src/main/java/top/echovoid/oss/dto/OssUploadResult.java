package top.echovoid.oss.dto;

import lombok.Builder;
import lombok.Data;
import top.echovoid.oss.OssClientType;

/**
 * @author Peng
 * @since 2024/9/25
 */
@Data
@Builder
public class OssUploadResult {
    private String key;
    private String url;
    private OssClientType type;
    private String bucketName;
    private String contentType;
    private long size;
    private String hash;
}