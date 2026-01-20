package top.echovoid.security.core.filter;

import com.nimbusds.jwt.JWTClaimsSet;
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
import top.echovoid.common.utils.JwtUtils;
import top.echovoid.security.core.JwtProps;

import java.io.IOException;
import java.util.Collections;

/**
 * @author Penn Collins
 * @since 2025/4/11
 */
@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProps jwtProps;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = JwtUtils.getToken(request);
        if (StringUtils.hasLength(token)) {
            try {
                JWTClaimsSet jwtClaimsSet = JwtUtils.parseToken(token, jwtProps.getSecret());
                String tokenType = jwtClaimsSet.getClaimAsString("token_type");
                if ("access".equalsIgnoreCase(tokenType)) {
                    Long userId = Long.valueOf(jwtClaimsSet.getSubject());
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}

