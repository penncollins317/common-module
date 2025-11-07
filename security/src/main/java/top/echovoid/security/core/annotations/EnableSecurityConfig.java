package top.echovoid.security.core.annotations;

import org.springframework.context.annotation.Import;
import top.echovoid.security.core.SecurityCoreAutoConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Peng
 * @since 2025/1/20
 */
@Import(SecurityCoreAutoConfig.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableSecurityConfig {
}
