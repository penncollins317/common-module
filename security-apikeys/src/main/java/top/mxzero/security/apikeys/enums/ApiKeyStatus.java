package top.mxzero.security.apikeys.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Peng
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
