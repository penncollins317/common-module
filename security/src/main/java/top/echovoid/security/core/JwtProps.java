package top.echovoid.security.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.UUID;

/**
 * @author Penn Collins
 * @since 2025/2/13
 */
@Data
@ConfigurationProperties("echovoid.security.jwt")
public class JwtProps {
    private String issuer = "passport.echovoid.top";
    private String secret = UUID.randomUUID().toString();
    private long expire = 7_200;
    private long refreshExpire = 43_200;
}
