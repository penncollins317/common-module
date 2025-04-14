package top.mxzero.security.core;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import top.mxzero.security.core.authentication.CustomAuthenticationFailHandler;
import top.mxzero.security.core.authentication.CustomAuthenticationSuccessHandler;
import top.mxzero.security.core.authentication.JsonAccessDeniedHandler;
import top.mxzero.security.core.authentication.JsonAuthenticationEntryPoint;
import top.mxzero.security.core.filter.JwtAuthenticationFilter;
import top.mxzero.security.core.service.LoginService;

import java.util.List;

@Slf4j
@EnableMethodSecurity
@EnableWebSecurity
@Configuration
@ComponentScan
@EnableConfigurationProperties(JwtProps.class)
public class SecurityCoreAutoConfig {
    private static final String DEFAULT_LOGIN_URl = "/login";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, LoginService loginService) throws Exception {
        AuthenticationSuccessHandler successHandler = new CustomAuthenticationSuccessHandler(loginService);
        AuthenticationFailureHandler failureHandler = new CustomAuthenticationFailHandler(DEFAULT_LOGIN_URl);
        JsonAuthenticationEntryPoint authenticationEntryPoint = new JsonAuthenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(DEFAULT_LOGIN_URl));
        JsonAccessDeniedHandler accessDeniedHandler = new JsonAccessDeniedHandler(new AccessDeniedHandlerImpl());
        http.authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/token/**", "/error", "/public/**").permitAll();
                    authorize.anyRequest().authenticated();
                })
                .formLogin(login -> {
                    login.loginPage(DEFAULT_LOGIN_URl).permitAll()
                            .successHandler(successHandler)
                            .failureHandler(failureHandler);
                })
                .oneTimeTokenLogin(oneTime -> {
                    oneTime.authenticationSuccessHandler(successHandler)
                            .authenticationFailureHandler(failureHandler)
                    ;
//                            .tokenGenerationSuccessHandler();
                })
                .exceptionHandling(handler -> {
                    handler.accessDeniedHandler(accessDeniedHandler);
                    handler.authenticationEntryPoint(authenticationEntryPoint);
                })
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/logout").permitAll())
//                .sessionManagement(session -> {
//                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                })
//                .requestCache(AbstractHttpConfigurer::disable)
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
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(List.of(daoAuthenticationProvider));
    }
}
