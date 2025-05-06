package top.mxzero.security.core.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import top.mxzero.security.core.utils.JwtUtil;
import top.mxzero.security.jwt.service.TokenService;

import java.io.IOException;
import java.util.Collections;

/**
 * @author Peng
 * @since 2025/4/11
 */
@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = JwtUtil.getToken(request);
        if (StringUtils.hasLength(token)) {
            try {
                Jws<Claims> claimsJws = tokenService.parseToken(token);
                String tokenType = claimsJws.getPayload().get("type", String.class);
                if ("access".equalsIgnoreCase(tokenType)) {
                    Long userId = Long.valueOf(claimsJws.getPayload().getSubject());
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (JwtException e) {
                log.debug(e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}

