package top.echovoid.security.oauth2.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * @author Penn Collins
 * @since 2025/4/11
 */
@Slf4j
@AllArgsConstructor
public class CustomJwtAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("customer jwt filter.");
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            log.info("exists authentication info.");
        } else {
            if (request.getParameter("u") != null){
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getParameter("u"), null, List.of());
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }

        filterChain.doFilter(request, response);
    }
}
