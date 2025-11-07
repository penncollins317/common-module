package top.echovoid.message.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.annotation.AnnotationTemplateExpressionDefaults;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Penn Collins
 * @since 2025/4/12
 */
@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> {
                    request.anyRequest().authenticated();
                })
//                .oauth2Login(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2 -> {
                    DefaultBearerTokenResolver tokenResolver = new DefaultBearerTokenResolver();
                    tokenResolver.setAllowUriQueryParameter(true);
                    oauth2.jwt(Customizer.withDefaults())
                            .bearerTokenResolver(tokenResolver);
                });
        return http.build();
    }

    @Bean
    public AnnotationTemplateExpressionDefaults templateExpressionDefaults() {
        return new AnnotationTemplateExpressionDefaults();
    }
}
