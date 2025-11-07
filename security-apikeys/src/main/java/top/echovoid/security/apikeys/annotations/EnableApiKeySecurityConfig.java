package top.echovoid.security.apikeys.annotations;

import org.springframework.context.annotation.Import;
import top.echovoid.security.apikeys.ApiKeysSecurityAutoConfig;
import top.echovoid.security.core.annotations.EnableSecurityConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Penn Collins
 * @since 2025/2/15
 */
@EnableSecurityConfig
@Import(ApiKeysSecurityAutoConfig.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableApiKeySecurityConfig{
}
