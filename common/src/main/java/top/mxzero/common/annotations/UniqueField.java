package top.mxzero.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注业务数据唯一值
 *
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2024/2/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UniqueField {
    /**
     * 以其他字段组成联合唯一索引
     */
    String[] union() default {};
}