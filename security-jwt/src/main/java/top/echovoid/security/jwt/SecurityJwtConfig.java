package top.echovoid.security.jwt;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.echovoid.security.jwt.service.TokenService;
import top.echovoid.security.jwt.service.impl.TokenServiceImpl;

/**
 * @author Penn Collins
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