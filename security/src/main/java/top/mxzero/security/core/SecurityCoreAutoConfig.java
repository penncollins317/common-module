package top.mxzero.security.core;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import top.mxzero.security.core.authentication.AccessTokenAuthenticationConverter;
import top.mxzero.security.core.authentication.JsonAccessDeniedHandler;
import top.mxzero.security.core.authentication.JsonAuthenticationEntryPoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import java.util.UUID;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
@ComponentScan
@EnableConfigurationProperties(JwtProps.class)
@MapperScan("top.mxzero.security.core.mapper")
public class SecurityCoreAutoConfig {
    private static final String PRIVATE_KEY_FILE = System.getProperty("user.dir") + File.separator + "temp" + File.separator + "key" + File.separator + "private.key";
    private static final String PUBLIC_KEY_FILE = System.getProperty("user.dir") + File.separator + "temp" + File.separator + "key" + File.separator + "public.key";

    private static KeyPair generateNewRsaKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException("Error generating RSA key pair", e);
        }
    }

    private static void saveKeyToFile(String fileName, byte[] key) {
        File file = new File(fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] loadKeyFromFile(String fileName) {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            return fis.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static KeyPair getKeyPair() {
        try {
            if (new File(PRIVATE_KEY_FILE).exists() && new File(PUBLIC_KEY_FILE).exists()) {
                byte[] privateKeyBytes = loadKeyFromFile(PRIVATE_KEY_FILE);
                byte[] publicKeyBytes = loadKeyFromFile(PUBLIC_KEY_FILE);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
                RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
                return new KeyPair(publicKey, privateKey);
            } else {
                KeyPair keyPair = generateNewRsaKey();
                saveKeyToFile(PRIVATE_KEY_FILE, keyPair.getPrivate().getEncoded());
                saveKeyToFile(PUBLIC_KEY_FILE, keyPair.getPublic().getEncoded());
                return keyPair;
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error loading or generating RSA key pair", e);
        }
    }

    private static final KeyPair KEY_PAIR;

    static {
        KEY_PAIR = getKeyPair();

    }

    @Bean
    public JwtEncoder jwtEncoder() {
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) KEY_PAIR.getPublic()).privateKey(KEY_PAIR.getPrivate()).keyID(UUID.randomUUID().toString()).build();
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(rsaKey)));
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        RSAPublicKey publicKey = (RSAPublicKey) KEY_PAIR.getPublic();
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JsonAuthenticationEntryPoint authenticationEntryPoint = new JsonAuthenticationEntryPoint();
        JsonAccessDeniedHandler accessDeniedHandler = new JsonAccessDeniedHandler();
        http.authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/token/**", "/public/**", "/error/**").permitAll();
                    authorize.anyRequest().authenticated();
                })
                .oauth2ResourceServer(resource -> {
                    DefaultBearerTokenResolver tokenResolver = new DefaultBearerTokenResolver();
                    tokenResolver.setAllowUriQueryParameter(true);
                    resource.jwt(jwt -> {
                        AccessTokenAuthenticationConverter converter = new AccessTokenAuthenticationConverter();
                        converter.setJwtGrantedAuthoritiesConverter(new JwtGrantedAuthoritiesConverter());
                        jwt.jwtAuthenticationConverter(converter);
                    }).bearerTokenResolver(tokenResolver);
                    resource.accessDeniedHandler(accessDeniedHandler);
                    resource.authenticationEntryPoint(authenticationEntryPoint);
                })
                .exceptionHandling(handler -> {
                    handler.accessDeniedHandler(accessDeniedHandler);
                    handler.authenticationEntryPoint(authenticationEntryPoint);
                })
                .formLogin(Customizer.withDefaults())
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .logout(AbstractHttpConfigurer::disable)
                .requestCache(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(List.of(daoAuthenticationProvider));
    }


}
