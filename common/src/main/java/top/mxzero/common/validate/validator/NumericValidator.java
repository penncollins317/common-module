package top.mxzero.common.validate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import top.mxzero.common.validate.Numeric;

public class NumericValidator implements ConstraintValidator<Numeric, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // 如果允许为空，则返回true
        }
        return value.matches("\\d+"); // 检查是否为纯数字
    }
}