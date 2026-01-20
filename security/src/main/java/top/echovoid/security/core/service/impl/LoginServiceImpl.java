package top.echovoid.security.core.service.impl;

import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.echovoid.common.exceptions.ServiceException;
import top.echovoid.common.utils.JwtUtils;
import top.echovoid.security.core.JwtProps;
import top.echovoid.security.core.dto.LoginRequestBody;
import top.echovoid.security.core.dto.TokenDTO;
import top.echovoid.security.core.service.LoginService;

import java.util.Map;

/**
 * @author Penn Collins
 * @since 2025/2/5
 */
@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final AuthenticationManager authenticationManager;
    private final JwtProps jwtProps;

    @Override
    public TokenDTO loginByUsername(LoginRequestBody args) {
        try {
            Authentication authenticate = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(args.getUsername(), args.getPassword()));
            return this.createToken(authenticate.getName());
        } catch (AuthenticationException e) {
            throw new ServiceException("账号或密码错误");
        }
    }

    @Override
    public TokenDTO getTokenByUserId(Long userId) {
        return this.createToken(userId.toString());
    }

    private TokenDTO createToken(String subject) {
        String accessToken = JwtUtils.createToken(subject, Map.of("token_type", "access"), jwtProps.getSecret(), jwtProps.getExpire());
        String refreshToken = JwtUtils.createToken(subject, Map.of("token_type", "refresh"), jwtProps.getSecret(), jwtProps.getRefreshExpire());
        return TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expireIn(jwtProps.getExpire())
                .refreshExpireIn(jwtProps.getRefreshExpire())
                .build();
    }

    @Override
    public TokenDTO refresh(String refreshToken) {
        try {
            JWTClaimsSet jwtClaimsSet = JwtUtils.parseToken(refreshToken, jwtProps.getSecret(), "auth");
            String tokenType = (String) jwtClaimsSet.getClaim("token_type");
            if (!"refresh".equalsIgnoreCase(tokenType)) {
                throw new ServiceException("refresh token type error.");
            }
            String subject = jwtClaimsSet.getSubject();
            long time = jwtClaimsSet.getExpirationTime().getTime();
            // refresh token有效期小于access token时，返回新的refresh token
            if (System.currentTimeMillis() + jwtProps.getExpire() * 1000 > time) {
                return this.createToken(subject);
            }

            return TokenDTO.builder()
                    .accessToken(JwtUtils.createToken(subject, Map.of("type_type", "access"), jwtProps.getSecret(), jwtProps.getExpire()))
                    .refreshToken(refreshToken)
                    .expireIn(jwtProps.getExpire())
                    .refreshExpireIn(jwtProps.getRefreshExpire())
                    .build();

        } catch (Exception e) {
            throw new ServiceException("Refresh token error.");
        }
    }
}
