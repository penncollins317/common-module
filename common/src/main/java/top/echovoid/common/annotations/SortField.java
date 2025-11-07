package top.echovoid.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于排序的字段
 *
 * @author Penn Collins
 * @email penncollins317@gmail.com
 * @since 2024/3/8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SortField {
}
