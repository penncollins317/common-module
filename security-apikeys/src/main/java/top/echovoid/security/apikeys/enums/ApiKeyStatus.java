package top.echovoid.security.apikeys.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Penn Collins
 * @since 2025/3/4
 */
@Getter
@AllArgsConstructor
public enum ApiKeyStatus {
    DISABLE(0),
    ACTIVE(1),
    EXPIRE(2)
    ;
    @EnumValue
    private final int value;
}
