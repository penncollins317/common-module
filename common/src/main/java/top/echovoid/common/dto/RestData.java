package top.echovoid.common.dto;

import lombok.Data;
import org.slf4j.MDC;
import top.echovoid.common.exceptions.ServiceException;
import top.echovoid.common.filter.RequestTraceFilter;

/**
 * @author Penn Collins
 * @email penncollins317@gmail.com
 * @since 2023/9/1
 */
@Data
public class RestData<T> {
    public static final int DEFAULT_ERROR_CODE = 999;
    public static final int DEFAULT_SUCCESS_CODE = 0;

    private String message;
    private T data;
    private int code;
    private String requestId;


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
        restData.setRequestId(MDC.get(RequestTraceFilter.TRACE_ID_KEY));
        return restData;
    }

    public static <T> RestData<T> success(T data, String message) {
        RestData<T> restData = new RestData<>();
        restData.setData(data);
        restData.setMessage(message);
        restData.setCode(DEFAULT_SUCCESS_CODE);
        restData.setRequestId(MDC.get(RequestTraceFilter.TRACE_ID_KEY));
        return restData;
    }

    public static <T> RestData<T> error(String errMsg) {
        return error(errMsg, DEFAULT_ERROR_CODE);
    }

    public static <T> RestData<T> error(String errMsg, int errorCode) {
        RestData<T> restData = new RestData<>();
        restData.setMessage(errMsg);
        restData.setCode(errorCode);
        restData.setRequestId(MDC.get(RequestTraceFilter.TRACE_ID_KEY));
        return restData;
    }

    public static <T> RestData<T> error(ServiceException exception) {
        RestData<T> restData = new RestData<>();
        restData.setMessage(exception.getMessage());
        restData.setCode(exception.getCode());
        restData.setRequestId(MDC.get(RequestTraceFilter.TRACE_ID_KEY));
        return restData;
    }
}
