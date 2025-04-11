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
import top.mxzero.security.core.enums.TokenType;
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
        String access = JwtUtil.createToken(UUID.randomUUID().toString(), subject, TokenType.ACCESS_TOKEN);
        String refresh = JwtUtil.createToken(UUID.randomUUID().toString(), subject, TokenType.REFRESH_TOKEN);
        return TokenDTO.builder().accessToken(access).refreshToken(refresh).expire(jwtProps.getExpire()).build();
    }


    @Override
    public TokenDTO refresh(String token) {
        try {
            Jws<Claims> claimsJws = JwtUtil.parseToken(token);
            String subject = claimsJws.getBody().getSubject();
            return this.createToken(subject);
        } catch (JwtException e) {
            throw new ServiceException("Refresh token error.");
        }
    }
}
