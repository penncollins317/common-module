package top.mxzero.security.apikeys.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import top.mxzero.security.apikeys.authencation.OutAppPrincipal;
import top.mxzero.security.apikeys.authencation.SignatureAuthenticationToken;
import top.mxzero.security.apikeys.entity.ApiKey;
import top.mxzero.security.apikeys.enums.ApiKeyStatus;
import top.mxzero.security.apikeys.mapper.ApiKeyMapper;

import java.io.IOException;

/**
 * @author Peng
 * @since 2025/3/17
 */
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
    public static final String AUTH_KEY = "x-api-key";
    private final ApiKeyMapper apiKeyMapper;
    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    public ApiKeyAuthenticationFilter(ApiKeyMapper apiKeyMapper) {
        this.apiKeyMapper = apiKeyMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String key = request.getHeader(AUTH_KEY);
        if (StringUtils.hasLength(key)) {
            ApiKey apiKey = apiKeyMapper.findByCode(key);
            if (apiKey != null && apiKey.getStatus() == ApiKeyStatus.ACTIVE) {
                OutAppPrincipal principal = new OutAppPrincipal(apiKey.getAppId(), null, "", "");
                SignatureAuthenticationToken authentication = new SignatureAuthenticationToken(principal, authenticationDetailsSource.buildDetails(request));
                authentication.setAuthenticated(true);
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
