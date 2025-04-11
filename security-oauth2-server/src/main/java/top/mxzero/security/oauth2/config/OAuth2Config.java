package top.mxzero.security.oauth2.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import top.mxzero.common.utils.MD5Util;
import top.mxzero.security.oauth2.authentication.DeviceClientAuthenticationConverter;
import top.mxzero.security.oauth2.authentication.DeviceClientAuthenticationProvider;
import top.mxzero.security.oauth2.federation.OidcUserInfoExt;

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
import java.time.Duration;
import java.util.List;
import java.util.UUID;

/**
 * @author Peng
 * @since 2025/4/4
 */
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
@Configuration
public class OAuth2Config {
    private static final String KEY_ID = "1190f087-ef78-47cf-bf20-1270fdea493c"; // 固定值
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
                log.info("load local key.");
                log.info("public key md5:{}", MD5Util.getMD5(publicKeyBytes));
                log.info("private key md5:{}", MD5Util.getMD5(privateKeyBytes));
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

    private final UserDetailsService userDetailsService;

    // http://localhost:9000/oauth2/authorize?client_id=oidc-client&redirect_uri=http://127.0.0.1:8080/login/oauth2/code/oidc-client&scope=openid profile&response_type=code
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();
        DeviceClientAuthenticationConverter deviceClientAuthenticationConverter =
                new DeviceClientAuthenticationConverter(
                        this.authorizationServerSettings().getDeviceAuthorizationEndpoint());
        DeviceClientAuthenticationProvider deviceClientAuthenticationProvider =
                new DeviceClientAuthenticationProvider(this.registeredClientRepository());
        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, (authorizationServer) -> {
                    authorizationServer
                            .authorizationEndpoint(
                                    authorization -> {
                                        authorization.authenticationProvider(new JwtAuthenticationProvider(this.jwtDecoder()));
                                    }
                            )
                            .clientAuthentication(clientAuthentication ->
                                    clientAuthentication
                                            .authenticationConverter(deviceClientAuthenticationConverter)
                                            .authenticationProvider(deviceClientAuthenticationProvider)
                            )
                            .oidc((oidc) -> oidc
                                    .userInfoEndpoint((userInfo) -> userInfo
                                            .userInfoMapper(new OidcUserInfoExt())
                                    )
                            );
                })
//                .addFilterBefore(new CustomJwtAuthorizationFilter(this.userDetailsService, this.jwtDecoder()), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .anyRequest().authenticated()
                )
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                );
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(
                            "/static/**", "/**.css", "/**.js", "/**.png", "/**.ico", "/login", "/logout").permitAll();
                    authorize.anyRequest().authenticated();
                })
                .formLogin(Customizer.withDefaults())
                .logout(logout -> {
                    logout.logoutUrl("/logout").permitAll();
                })
//                .oauth2Login(
//                        oauth -> {
//                            oauth.loginPage("/login");
//                            oauth.tokenEndpoint(token -> {
//                                token.accessTokenResponseClient(new OAuth2AccessTokenResponseClientDecoderAdaptor());
//                            });
//                            oauth.userInfoEndpoint(userinfo -> {
//                                userinfo.userService(new DefaultOAuth2UserServiceDecoderAdaptor(oAuth2UserRelatedMapper, userMapper));
//                            });
//                        }
//                )
//                .oauth2ResourceServer(resource -> {
//                    DefaultBearerTokenResolver tokenResolver = new DefaultBearerTokenResolver();
//                    tokenResolver.setAllowUriQueryParameter(true);
//                    resource
//                            .jwt(Customizer.withDefaults())
//                            .bearerTokenResolver(tokenResolver);
//                    resource.accessDeniedHandler(new JsonAccessDeniedHandler());
//                    resource.authenticationEntryPoint(new JsonAuthenticationEntryPoint());
//                })
                .csrf(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        TokenSettings tokenSettings = TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofHours(2))
                .refreshTokenTimeToLive(Duration.ofDays(15))
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) // JWT令牌
                .build();
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("oidc-client")
                .clientSecret(this.passwordEncoder().encode("secret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/oidc-client")
                .postLogoutRedirectUri("http://127.0.0.1:8080/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .tokenSettings(tokenSettings)
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                .build();

        RegisteredClient openapiClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("openapi-client")
                .clientSecret(this.passwordEncoder().encode("secret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope("openapi")
                .tokenSettings(tokenSettings)
                .build();

        RegisteredClient deviceClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("device-messaging-client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.DEVICE_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .scope("message:read")
                .scope("message:write")
                .tokenSettings(tokenSettings)
                .build();

        RegisteredClient spaClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("spa-client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://localhost:3000/login/oauth2/callback")
                .postLogoutRedirectUri("http://loclhost:3000/login")
                .scope(OidcScopes.OPENID)
                .tokenSettings(tokenSettings)
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();
        return new InMemoryRegisteredClientRepository(oidcClient, openapiClient, deviceClient, spaClient);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) KEY_PAIR.getPublic()).privateKey(KEY_PAIR.getPrivate()).keyID(KEY_ID).build();
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(rsaKey)));
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        RSAPublicKey publicKey = (RSAPublicKey) KEY_PAIR.getPublic();
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public HttpSessionEventPublisher sessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAPublicKey publicKey = (RSAPublicKey) KEY_PAIR.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) KEY_PAIR.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(KEY_ID)
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(List.of(daoAuthenticationProvider));
    }


}