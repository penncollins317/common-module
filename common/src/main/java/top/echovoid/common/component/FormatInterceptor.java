package top.echovoid.common.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

public class FormatInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取 URL 参数中的 `format` 值
        String format = request.getParameter("format");
        // 根据 `format` 参数设置响应的 Content-Type
        if ("xml".equalsIgnoreCase(format)) {
            response.setContentType(MediaType.APPLICATION_XML_VALUE);
        } else if("json".equalsIgnoreCase(format)){
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        }

        // 返回 true 继续请求的处理
        return true;
    }
}