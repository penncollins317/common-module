package top.mxzero.security.core.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Peng
 * @since 2025/3/19
 */
@AllArgsConstructor
@Getter
public enum OAuth2Provider {
    GITHUB("github"),
    WEIBO("weibo"),
    QQ("qq"),
    WECHAT("wechat"),
    ;

    @EnumValue
    private final String value;
}