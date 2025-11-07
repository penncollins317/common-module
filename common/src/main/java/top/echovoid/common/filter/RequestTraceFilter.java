package top.echovoid.common.filter;

import ch.qos.logback.core.util.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Penn Collins
 * @since 2025/5/22
 */
@Slf4j
public class RequestTraceFilter extends HttpFilter {
    public static final String TRACE_ID_KEY = "traceId";
    public static final String REQUEST_HEADER_TRACE_ID_KEY = "x-request-id";

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestTraceId = request.getHeader(REQUEST_HEADER_TRACE_ID_KEY);
        if(!StringUtils.hasText(requestTraceId)){
            requestTraceId = UUID.randomUUID().toString();
        }
        MDC.put(TRACE_ID_KEY, requestTraceId);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_ID_KEY);
        }
    }
}
