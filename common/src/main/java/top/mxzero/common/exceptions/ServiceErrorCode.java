package top.mxzero.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2023/9/13
 */
@Getter
@AllArgsConstructor
public enum ServiceErrorCode {

    TOKEN_EXPIRE(6, "Token过期"),
    USER_LOCKED(7, "用户被锁定"),
    USER_NO_AUTH(8, "用户未认证"),

    PASSWORD_ERROR(9, "密码错误"),

    PASSWORD_CONFIRM(10, "两次密码不一致"),

    ACCOUNT_LOCKED(11, "账号被锁定"),

    PASSWORD_REQUIRED_UPDATE(12, "密码需要更新"),

    RESOURCE_NOT_FOUND(404, "访问的资源不存在"),

    SYSTEM_ERROR(999, "系统错误");
    private int code;
    private String message;
}

