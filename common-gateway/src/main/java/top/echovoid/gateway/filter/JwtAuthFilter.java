package top.echovoid.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.echovoid.security.jwt.service.TokenService;

import java.nio.charset.StandardCharsets;
import java.util.Set;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {
    @Autowired
    private TokenService tokenService;

    private final AntPathMatcher matcher = new AntPathMatcher();
    @Value("${echovoid.gateway.auth.whitelist}")
    private Set<String> whitelist;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 1. 白名单放行
        if (whitelist.stream().anyMatch(p -> matcher.match(p, path))) {
            return chain.filter(exchange);
        }

        // 2. 获取 Authorization 头
        String token;
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            token = exchange.getRequest().getQueryParams().getFirst("access_token");
            if (!StringUtils.hasLength(token)) {
                return unauthorized(exchange, "Missing or invalid token");
            }
        } else {
            token = authHeader.split(" ")[1];
        }
        try {
            Jws<Claims> claimsJws = tokenService.parseToken(token);

            String userId = claimsJws.getPayload().getSubject();
            // 4. 构造新请求，添加用户信息头
            ServerHttpRequest newRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", userId)
                    .build();

            ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
            return chain.filter(newExchange);

        } catch (Exception e) {
            return unauthorized(exchange, "Invalid or expired token");
        }
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String msg) {
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] bytes = ("{\"error\": \"" + msg + "\"}").getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory().wrap(bytes)));
    }

    @Override
    public int getOrder() {
        return -1; // 确保早执行
    }
}
