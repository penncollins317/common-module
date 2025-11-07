package top.echovoid.security.oauth2.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.security.oauth2.server.authorization.web.OAuth2AuthorizationEndpointFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Penn Collins
 * @since 2025/4/11
 */
@AllArgsConstructor
public class OAuth2AuthorizationEndpointFilterExtend extends OncePerRequestFilter implements Ordered {
    private final OAuth2AuthorizationEndpointFilter endpointFilter;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        this.endpointFilter.doFilter(request, response, filterChain);
    }
}
