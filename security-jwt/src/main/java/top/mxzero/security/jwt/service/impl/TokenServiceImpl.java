package top.mxzero.security.jwt.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import top.mxzero.security.jwt.JwtProps;
import top.mxzero.security.jwt.dto.TokenDTO;
import top.mxzero.security.jwt.service.TokenService;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author Peng
 * @since 2025/5/7
 */
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtProps jwtProps;

    private SecretKey generaKey() {
        return Keys.hmacShaKeyFor(jwtProps.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String createToken(String tokenId, String subject, long expireIn, Map<String, Object> extraData) {
        Date currentDate = new Date();
        JwtBuilder jwtBuilder = Jwts.builder().signWith(generaKey(), Jwts.SIG.HS256)
                .subject(subject)
                .issuer(jwtProps.getIssuer())
                .expiration(new Date(currentDate.getTime() + expireIn * 1000))
                .id(tokenId)
                .issuedAt(currentDate);
        if (extraData != null && !extraData.isEmpty()) {
            jwtBuilder.claims(extraData);
        }
        return jwtBuilder.compact();
    }

    @Override
    public TokenDTO createFullToken(String subject) {
        long current = System.currentTimeMillis();
        String access = this.createToken(UUID.randomUUID().toString(), subject, jwtProps.getExpire(), Map.of("type", "access"));
        String refresh = this.createToken(UUID.randomUUID().toString(), subject, jwtProps.getExpire() * jwtProps.getRefreshRate(), Map.of("type", "refresh"));
        return TokenDTO.builder().accessToken(access).refreshToken(refresh)
                .expire(jwtProps.getExpire())
                .expireTime(new Date(current + jwtProps.getExpire() * 1000))
                .refreshExpireIn(jwtProps.getExpire() * jwtProps.getRefreshRate())
                .refreshExpireTime(new Date(current + jwtProps.getExpire() * jwtProps.getRefreshRate() * 1000))
                .build();
    }

    @Override
    public boolean verifyToken(String token) {
        try {
            Jwts.parser().verifyWith(generaKey()).build().parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Jws<Claims> parseToken(String token) {
        return Jwts.parser().verifyWith(generaKey()).build().parseSignedClaims(token);
    }
}
