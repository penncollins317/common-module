package top.mxzero.security.core.enums;

import lombok.Getter;

/**
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2022/11/13
 */
@Getter
public enum TokenType {
    ACCESS_TOKEN("access_token"),
    REFRESH_TOKEN("refresh_token");
    private final String value;

    TokenType(String type) {
        this.value = type;
    }

}
