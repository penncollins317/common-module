package top.mxzero.mcp.weather.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Peng
 * @since 2025/4/21
 */
@Data
@ConfigurationProperties(prefix = "mxzero.amap")
public class AMAPConfigProperties {
    private String webServiceKey;
}
