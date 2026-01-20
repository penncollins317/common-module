package top.echovoid.security.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import top.echovoid.security.core.authentication.JsonAccessDeniedHandler;
import top.echovoid.security.core.authentication.JsonAuthenticationEntryPoint;
import top.echovoid.security.core.filter.JwtAuthenticationFilter;

import java.util.List;

@Slf4j
@EnableMethodSecurity
@EnableWebSecurity
@Configuration
@ComponentScan
@EnableConfigurationProperties(JwtProps.class)
public class SecurityCoreAutoConfig {
    @Bean
    public SecurityConfigAggregator securityConfigAggregator(@Autowired List<SecurityConfigProvider> securityConfigProviders) {
        return new SecurityConfigAggregator(securityConfigProviders);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtProps jwtProps, SecurityConfigAggregator aggregator, ObjectMapper objectMapper) throws Exception {
        JsonAuthenticationEntryPoint authenticationEntryPoint = new JsonAuthenticationEntryPoint(objectMapper);
        JsonAccessDeniedHandler accessDeniedHandler = new JsonAccessDeniedHandler(objectMapper);
        http.authorizeHttpRequests(authorize -> {
                    if (!aggregator.getRoleBasedUrls().isEmpty()) {
                        aggregator.getRoleBasedUrls().forEach(((role, urls) -> authorize.requestMatchers(urls.toArray(new String[0])).hasRole(role)));
                    }
                    authorize.requestMatchers(aggregator.getIgnoreUrls().toArray(new String[0])).permitAll();
                    authorize.requestMatchers("/token/**", "/error", "/favicon.ico", "/auth/login", "/public/register").permitAll();
                    authorize.anyRequest().authenticated();
                })
                .exceptionHandling(handler -> {
                    handler.accessDeniedHandler(accessDeniedHandler);
                    handler.authenticationEntryPoint(authenticationEntryPoint);
                })
                .addFilterAt(new JwtAuthenticationFilter(jwtProps), UsernamePasswordAuthenticationFilter.class)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .requestCache(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable);
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(List.of(daoAuthenticationProvider));
    }
}
