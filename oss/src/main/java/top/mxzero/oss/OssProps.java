package top.mxzero.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Peng
 * @since 2024/9/21
 */
@Data
@ConfigurationProperties("mxzero.oss")
public class OssProps {
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String endpoint;
    private String region;
    private boolean secret;
    private String type;
}
