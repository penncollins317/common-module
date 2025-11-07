package top.echovoid.common.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import top.echovoid.common.validate.validator.NumericValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NumericValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Numeric {
    String message() default "必须为纯数字";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}