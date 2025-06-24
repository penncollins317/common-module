package top.mxzero.common.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.exceptions.ServiceException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhang Peng
 * @email qianmeng6879@163.com
 * @since 2023/9/8
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    public RestData<?> handleAllException(Exception e) {
        e.printStackTrace();
        log.error("{}:{}", e.getClass().getName(), e.getMessage());
        return RestData.error("系统错误", 500);
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class})
    public RestData<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletResponse response) {
        HttpStatusCode statusCode = e.getStatusCode();
        return RestData.error(e.getMessage(), statusCode.value());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public RestData<?> handleBadCredentialsException(BadCredentialsException e) {
        return RestData.error(e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public RestData<?> handleAuthenticationException(AuthenticationException e) {
        return RestData.error(e.getMessage(), 401);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public RestData<?> handleAccessDeniedException() {
        return RestData.error("无权访问", 403);
    }

    @ExceptionHandler({AuthenticationCredentialsNotFoundException.class})
    public RestData<?> handleNoAuthenticateException() {
        return RestData.error("用户未登录", 401);
    }

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class, HttpMediaTypeNotAcceptableException.class})
    public RestData<?> handleNoHandlerFoundException(HttpServletRequest request) {
        return RestData.error(request.getRequestURI() + " not found", HttpServletResponse.SC_NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public RestData<?> handleHttpRequestMethodNotSupportedException() {
        return RestData.error("请求方法不支持", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public RestData<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return RestData.error(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @ExceptionHandler(ServiceException.class)
    public RestData<?> handleServiceException(ServiceException e, HttpServletResponse response) {
        return RestData.error(e.getMessage(), e.getCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestData<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletResponse response) {
        Map<String, Object> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return RestData.ok(errors, 422, "参数验证错误");
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public RestData<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return RestData.error(e.getMessage(), 422);
    }
}
