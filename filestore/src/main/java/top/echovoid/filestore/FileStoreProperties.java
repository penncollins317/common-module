package top.echovoid.filestore;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Penn Collins
 * @since 2025/9/28
 */
@Data
@ConfigurationProperties("echovoid.filestore")
public class FileStoreProperties {
    private String type = "local";
}