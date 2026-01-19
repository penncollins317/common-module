package top.echovoid.common.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Penn Collins
 * @since 2026/1/20
 */
public class JwtUtils {
    private JwtUtils() {
    }

    public static String createToken(String subject, String secret, long expirationSeconds) {
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("HS256 secret 必须至少 32 字节");
        }
        try {
            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                    .jwtID(UUID.randomUUID().toString())
                    .subject(subject)
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plusSeconds(expirationSeconds)));


            return sign(builder.build(), secret);
        } catch (JOSEException e) {
            throw new RuntimeException("JWT 签名生成失败", e);
        }
    }

    public static String createToken(String subject, Map<String, Object> claims, String secret, long expirationSeconds) {
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("HS256 secret 必须至少 32 字节");
        }
        try {
            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                    .jwtID(UUID.randomUUID().toString())
                    .subject(subject)
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plusSeconds(expirationSeconds)));

            if (claims != null && !claims.isEmpty()) {
                claims.forEach(builder::claim);
            }

            return sign(builder.build(), secret);
        } catch (JOSEException e) {
            throw new RuntimeException("JWT 签名生成失败", e);
        }
    }

    public static String createToken(String subject, String audience, Map<String, Object> claims, String secret, long expirationSeconds) {
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("HS256 secret 必须至少 32 字节");
        }
        try {
            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                    .jwtID(UUID.randomUUID().toString())
                    .subject(subject)
                    .audience(audience)
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plusSeconds(expirationSeconds)));

            if (claims != null && !claims.isEmpty()) {
                claims.forEach(builder::claim);
            }
            return sign(builder.build(), secret);
        } catch (JOSEException e) {
            throw new RuntimeException("JWT 签名生成失败", e);
        }
    }


    public static JWTClaimsSet parseToken(String token, String secret) {
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("HS256 secret 必须至少 32 字节");
        }
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secret.getBytes());

            if (!signedJWT.verify(verifier)) {
                throw new RuntimeException("Token 签名验证失败");
            }

            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            if (isExpired(claimsSet)) {
                throw new RuntimeException("Token 已过期");
            }

            return claimsSet;
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException("Token 解析异常", e);
        }
    }

    public static JWTClaimsSet parseToken(String token, String secret, String expectedAudience) {
        JWTClaimsSet claimsSet = parseToken(token, secret);
        List<String> audience = claimsSet.getAudience();
        if (audience == null || !audience.contains(expectedAudience)) {
            throw new RuntimeException("Audience 不匹配");
        }
        return claimsSet;
    }

    public static boolean isExpired(JWTClaimsSet claimsSet) {
        Date exp = claimsSet.getExpirationTime();
        if (exp == null) {
            throw new RuntimeException("Token 缺少 exp");
        }
        // +30秒防止服务器时间轻微漂移
        return new Date().after(new Date(exp.getTime() + 30_000));
    }

    private static String sign(JWTClaimsSet claimsSet, String secret) throws JOSEException {
        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .build(), claimsSet);
        JWSSigner signer = new MACSigner(secret.getBytes());
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }
}