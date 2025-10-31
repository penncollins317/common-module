package top.mxzero.filestore;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Peng
 * @since 2025/9/28
 */
@Data
@ConfigurationProperties("mxzero.filestore")
public class FileStoreProperties {
    private String type = "local";
}