package top.echovoid.mcp.weather.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Penn Collins
 * @since 2025/4/21
 */
@Data
@ConfigurationProperties(prefix = "echovoid.amap")
public class AMAPConfigProperties {
    private String webServiceKey;
}
