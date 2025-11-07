package top.echovoid.service.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Penn Collins
 * @since 2025/4/13
 */
@AllArgsConstructor
@Getter
public enum AccountType {
    EMAIL("email"),
    PHONE("phone"),
    ;

    @EnumValue
    private final String value;
}
