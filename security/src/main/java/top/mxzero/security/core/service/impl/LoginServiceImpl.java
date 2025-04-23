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
import top.mxzero.security.core.JwtProps;
import top.mxzero.security.core.dto.LoginRequestBody;
import top.mxzero.security.core.dto.TokenDTO;
import top.mxzero.security.core.service.LoginService;
import top.mxzero.security.core.utils.JwtUtil;
import top.mxzero.service.user.entity.User;
import top.mxzero.service.user.mapper.UserMapper;

import java.util.Date;
import java.util.UUID;

/**
 * @author Peng
 * @since 2025/2/5
 */
@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final JwtProps jwtProps;
    private static final char ACCESS_FLAG = 'a';
    private static final char REFRESH_FLAG = 'f';

    @Override
    public TokenDTO loginByUsername(LoginRequestBody args) {
        try {
            Authentication authenticate = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(args.getUsername(), args.getPassword()));
            User user = new User();
            user.setId(Long.valueOf(authenticate.getName()));
            user.setLastLoginAt(new Date());
            this.userMapper.updateById(user);
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
        char[] accessTokenId = UUID.randomUUID().toString().toCharArray();
        accessTokenId[accessTokenId.length - 1] = ACCESS_FLAG;
        char[] refreshTokenId = UUID.randomUUID().toString().toCharArray();
        refreshTokenId[refreshTokenId.length - 1] = REFRESH_FLAG;
        long current = System.currentTimeMillis();
        String access = JwtUtil.createToken(new String(accessTokenId), subject, jwtProps.getExpire());
        String refresh = JwtUtil.createToken(new String(refreshTokenId), subject, jwtProps.getExpire() * jwtProps.getRefreshRate());
        return TokenDTO.builder().accessToken(access).refreshToken(refresh)
                .expire(jwtProps.getExpire())
                .expireTime(new Date(current + jwtProps.getExpire() * 1000))
                .refreshExpireIn(jwtProps.getExpire() * jwtProps.getRefreshRate())
                .refreshExpireTime(new Date(current + jwtProps.getExpire() * jwtProps.getRefreshRate() * 1000))
                .build();
    }

    @Override
    public TokenDTO refresh(String token) {
        try {

            Jws<Claims> claimsJws = JwtUtil.parseToken(token);
            char[] charArray = claimsJws.getPayload().getId().toCharArray();
            char c = charArray[charArray.length - 1];
            if (c != REFRESH_FLAG) {
                throw new ServiceException("refresh token type error.");
            }
            String subject = claimsJws.getPayload().getSubject();
            return this.createToken(subject);
        } catch (JwtException e) {
            throw new ServiceException("Refresh token error.");
        }
    }
}
