package top.echovoid.security.apikeys.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Penn Collins
 * @since 2025/2/15
 */
@AllArgsConstructor
@Getter
public enum OutAppStatus {
    LOCKED(0),
    APPROVE(1),
    ACTIVE(2),
    DISABLE(3);

    @EnumValue
    private final int value;
}
