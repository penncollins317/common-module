package top.mxzero.security.jwt;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.mxzero.security.jwt.service.TokenService;
import top.mxzero.security.jwt.service.impl.TokenServiceImpl;

/**
 * @author Peng
 * @since 2025/5/7
 */
@Configuration
@EnableConfigurationProperties(JwtProps.class)
public class SecurityJwtConfig {
    @Bean
    public TokenService tokenService(JwtProps jwtProps) {
        return new TokenServiceImpl(jwtProps);
    }
}