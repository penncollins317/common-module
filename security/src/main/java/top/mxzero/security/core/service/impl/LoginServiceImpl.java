package top.mxzero.security.core.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.mxzero.common.exceptions.ServiceException;
import top.mxzero.security.core.dto.LoginRequestBody;
import top.mxzero.security.core.service.LoginService;
import top.mxzero.security.jwt.JwtProps;
import top.mxzero.security.jwt.dto.TokenDTO;
import top.mxzero.security.jwt.service.TokenService;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author Peng
 * @since 2025/2/5
 */
@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtProps jwtProps;
    private static final char ACCESS_FLAG = 'a';
    private static final char REFRESH_FLAG = 'f';

    @Override
    public TokenDTO loginByUsername(LoginRequestBody args) {
        try {
            Authentication authenticate = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(args.getUsername(), args.getPassword()));
            return this.createToken(authenticate.getName());
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("账号或密码错误");
        }
    }

    @Override
    public TokenDTO getTokenByUserId(Long userId) {
        return this.createToken(userId.toString());
    }

    private TokenDTO createToken(String subject) {
        return tokenService.createFullToken(subject);
    }

    @Override
    public TokenDTO refresh(String token) {
        try {
            Jws<Claims> claimsJws = tokenService.parseToken(token);
            String type = (String) claimsJws.getPayload().get("type");
            if (!"refresh".equalsIgnoreCase(type)) {
                throw new ServiceException("refresh token type error.");
            }

            String subject = claimsJws.getPayload().getSubject();

            long time = claimsJws.getPayload().getExpiration().getTime();
            // refresh token有效期小于access token时，返回新的refresh token
            if (System.currentTimeMillis() + jwtProps.getExpire() * 1000 > time) {
                return this.createToken(subject);
            }

            String accessToken = tokenService.createToken(UUID.randomUUID().toString(), subject, jwtProps.getExpire(), Map.of("type", "access"));
            return TokenDTO.builder()
                    .accessToken(accessToken)
                    .expire(jwtProps.getExpire())
                    .expireTime(new Date(System.currentTimeMillis() + jwtProps.getExpire() * 1000))
                    .refreshToken(token)
                    .refreshExpireTime(claimsJws.getPayload().getExpiration())
                    .refreshExpireIn((time - System.currentTimeMillis()) / 1000)
                    .build();
        } catch (JwtException e) {
            throw new ServiceException("Refresh token error.");
        }
    }
}
