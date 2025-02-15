package top.mxzero.security.apikeys;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import top.mxzero.security.apikeys.filter.SignatureAuthenticationFilter;
import top.mxzero.security.apikeys.mapper.OutAppMapper;

/**
 * @author Peng
 * @since 2025/1/20
 */
@MapperScan("top.mxzero.security.apikeys.mapper")
@Configuration
@ComponentScan
public class ApiKeysSecurityAutoConfig {

    @Bean
    public SecurityFilterChain openapiSecurityFilterChain(HttpSecurity http, OutAppMapper outAppMapper) throws Exception {
        http
                .securityMatcher("/openapi/**")
                .authorizeHttpRequests(authorize -> {
                    authorize.anyRequest().authenticated();
                })
                .exceptionHandling(handler -> {
                    handler.accessDeniedHandler(((request, response, accessDeniedException) -> {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    }));
                    handler.authenticationEntryPoint(((request, response, authException) -> {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    }));
                })
                .logout(AbstractHttpConfigurer::disable)
                .requestCache(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .addFilterAt(new SignatureAuthenticationFilter(outAppMapper), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
