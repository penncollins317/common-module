package top.mxzero.common.dto;

import lombok.Data;
import top.mxzero.common.exceptions.ServiceException;

/**
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2023/9/1
 */
@Data
public class RestData<T> {
    public static final int DEFAULT_ERROR_CODE = 999;
    public static final int DEFAULT_SUCCESS_CODE = 0;

    private String message;
    private T data;
    private int code;


    public static <T> RestData<T> ok(T data) {
        return success(data);
    }

    public static <T> RestData<T> ok(T data, int code) {
        return ok(data, code, "success");
    }

    public static <T> RestData<T> ok(T data, int code, String message) {
        RestData<T> restData = ok(data);
        restData.code = code;
        restData.message = message;
        return restData;
    }

    public static <T> RestData<T> success() {
        return success(null);
    }

    public static <T> RestData<T> success(T data) {
        RestData<T> restData = new RestData<>();
        restData.setData(data);
        restData.setMessage("success");
        restData.setCode(DEFAULT_SUCCESS_CODE);
        return restData;
    }

    public static <T> RestData<T> success(T data, String message) {
        RestData<T> restData = new RestData<>();
        restData.setData(data);
        restData.setMessage(message);
        restData.setCode(DEFAULT_SUCCESS_CODE);
        return restData;
    }

    public static <T> RestData<T> error(String errMsg) {
        return error(errMsg, DEFAULT_ERROR_CODE);
    }

    public static <T> RestData<T> error(String errMsg, int errorCode) {
        RestData<T> restData = new RestData<>();
        restData.setMessage(errMsg);
        restData.setCode(errorCode);
        return restData;
    }

    public static <T> RestData<T> error(ServiceException exception) {
        RestData<T> restData = new RestData<>();
        restData.setMessage(exception.getMessage());
        restData.setCode(exception.getCode());
        return restData;
    }
}
