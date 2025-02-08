package top.mxzero.common.utils;

import com.alibaba.fastjson2.JSON;
import top.mxzero.common.exceptions.ServiceErrorCode;
import top.mxzero.common.exceptions.ServiceException;

import java.util.Map;

/**
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2024/3/14
 */
public class ErrorUtils {
    private ErrorUtils() {
    }

    public static void notFound() {
        throw new ServiceException(ServiceErrorCode.RESOURCE_NOT_FOUND);
    }

    public static void service(String message) {
        throw new ServiceException(message);
    }

    public static void fieldValidatedError(Map<String, Object> data) {
        throw new ServiceException(JSON.toJSONString(data));
    }
}

