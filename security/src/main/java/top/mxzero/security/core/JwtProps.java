package top.mxzero.security.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Peng
 * @since 2025/2/13
 */
@Data
@ConfigurationProperties("mxzero.jwt")
public class JwtProps {
    private String issuer;
    private String secret;
    private long expire = 7200L;
    private int refreshRate = 3;
}
