package top.echovoid.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.UUID;

/**
 * @author Peng
 * @since 2025/2/13
 */
@Data
@ConfigurationProperties("mxzero.jwt")
public class JwtProps {
    private String issuer = "default";
    private String secret = UUID.randomUUID().toString();
    private long expire = 7200L;
    private int refreshRate = 3;
}
