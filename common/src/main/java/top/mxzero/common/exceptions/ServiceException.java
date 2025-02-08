package top.mxzero.common.exceptions;

import lombok.Getter;
import top.mxzero.common.dto.RestData;

/**
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2023/8/21
 */
@Getter
public class ServiceException extends RuntimeException {
    /**
     * 错误状态码
     */
    private final int code;

    public ServiceException(ServiceErrorCode errorCode) {
        this(errorCode.getMessage(), errorCode.getCode());
    }

    public ServiceException(String message) {
        this(message, RestData.DEFAULT_ERROR_CODE);
    }

    public ServiceException(String message, int code) {
        super(message);
        this.code = code;
    }

    public ServiceException(String message, ServiceErrorCode errorCode) {
        this(message, errorCode.getCode());
    }
}
