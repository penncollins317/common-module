package top.echovoid.common.advice;

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
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import top.echovoid.common.dto.ApiErrorResponse;
import top.echovoid.common.dto.RestData;
import top.echovoid.common.exceptions.ServiceException;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Penn Collins
 * @email penncollins317@gmail.com
 * @since 2023/9/8
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public RestData<?> handleAllException(Exception e) {
        log.error(e.getMessage(), e);
        return RestData.error("System error.", 500);
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class})
    public RestData<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletResponse response) {
        HttpStatusCode statusCode = e.getStatusCode();
        response.setStatus(statusCode.value());
        return RestData.error(e.getMessage(), statusCode.value());
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(BadCredentialsException.class)
//    public RestData<?> handleBadCredentialsException(BadCredentialsException e) {
//        return RestData.error(e.getMessage());
//    }

//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(AuthenticationException.class)
//    public RestData<?> handleAuthenticationException(AuthenticationException e) {
//        return RestData.error(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
//    }

//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    @ExceptionHandler(AccessDeniedException.class)
//    public RestData<?> handleAccessDeniedException(AccessDeniedException e) {
//        return RestData.error(e.getMessage(), HttpStatus.FORBIDDEN.value());
//    }

//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler({AuthenticationCredentialsNotFoundException.class})
//    public RestData<?> handleNoAuthenticateException() {
//        return RestData.error("用户未登录", HttpStatus.UNAUTHORIZED.value());
//    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class, HttpMediaTypeNotAcceptableException.class})
    public RestData<?> handleNoHandlerFoundException(HttpServletRequest request) {
        return RestData.error(request.getRequestURI() + " not found", HttpServletResponse.SC_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public RestData<?> handleHttpRequestMethodNotSupportedException() {
        return RestData.error("method not Support.", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e,
                                                                  HttpServletRequest request) {
        List<Map<String, String>> errors = List.of(
                Map.of("location", "body", "message", "请求体格式不正确或无法解析")
        );

        return ApiErrorResponse.builder()
                .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .message("请求体格式错误")
                .errors(errors)
                .path(request.getRequestURI())
                .timestamp(new Date())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceException.class)
    public RestData<?> handleServiceException(ServiceException e) {
        return RestData.error(e.getMessage(), e.getCode());
    }


    /**
     * 处理 @Valid 参数校验失败异常
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                  HttpServletRequest request) {

        String location;
        if (e.getParameter().hasParameterAnnotation(RequestBody.class)) {
            location = "body";
        } else {
            location = "form";
        }

        // 合并同字段错误
        Map<String, String> mergedErrors = e.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        LinkedHashMap::new,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.joining("；"))
                ));

        // 构造返回列表
        List<Map<String, String>> errors = mergedErrors.entrySet()
                .stream()
                .map(entry -> Map.of(
                        "field", entry.getKey(),
                        "message", entry.getValue(),
                        "location", location
                ))
                .collect(Collectors.toList());

        return ApiErrorResponse.builder()
                .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .message("参数验证失败")
                .errors(errors)
                .path(request.getRequestURI())
                .timestamp(new Date())
                .build();
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MissingRequestValueException.class)
    public ApiErrorResponse handleMissingRequestValueException(MissingRequestValueException e,
                                                               HttpServletRequest request) {
        String location = "unknown";
        String name = "unknown";

        if (e instanceof MissingServletRequestParameterException ex) {
            location = "query";
            name = ex.getParameterName();
        } else if (e instanceof MissingRequestHeaderException ex) {
            location = "header";
            name = ex.getHeaderName();
        } else if (e instanceof MissingRequestCookieException ex) {
            location = "cookie";
            name = ex.getCookieName();
        }

        List<Map<String, String>> errors = List.of(
                Map.of(
                        "location", location,
                        "name", name,
                        "message", e.getMessage()
                )
        );

        return ApiErrorResponse.builder()
                .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .message("请求缺少必要的参数/头部/Cookie")
                .errors(errors)
                .path(request.getRequestURI())
                .timestamp(new Date())
                .build();
    }

}
