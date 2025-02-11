package top.mxzero.security.core.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.mxzero.security.core.dto.LoginRequestBody;
import top.mxzero.security.core.dto.TokenDTO;
import top.mxzero.security.core.entity.User;
import top.mxzero.security.core.mapper.UserMapper;
import top.mxzero.security.core.service.LoginService;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Peng
 * @since 2025/2/5
 */
@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private static final String ISSUER = "http://api.mxzero.top";

    @Override
    @Transactional
    public TokenDTO loginByUsername(LoginRequestBody args) {
        try {
            Authentication authenticate = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(args.getUsername(), args.getPassword()));
            User user = new User();
            user.setId(Long.valueOf(authenticate.getName()));
            user.setLastLoginAt(new Date());
            this.userMapper.updateById(user);
            return this.createToken(authenticate.getName(), args.getScope(), "1");
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("账号或密码错误");
        }
    }


    private String genaraJwt(String subject, Map<String, String> claims, long expireSeconds) {
        JwtClaimsSet.Builder claimBuilder = JwtClaimsSet.builder()
                .id(UUID.randomUUID().toString())
                .subject(subject)
                .issuer(ISSUER)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(expireSeconds))
                .claims(c -> c.putAll(claims));

        return jwtEncoder.encode(JwtEncoderParameters.from(claimBuilder.build())).getTokenValue();
    }

    private TokenDTO createToken(String subject, String scope, String version) {
        Map<String, String> claims = new HashMap<>();
        claims.put("token_type", "access");
        claims.put("scope", scope);
        claims.put("version", version);
        String accessToken = this.genaraJwt(subject, claims, 7200);

        claims.put("token_type", "refresh");
        String refreshToken = this.genaraJwt(subject, claims, 3600 * 24 * 7);
        return TokenDTO.builder().accessToken(accessToken).refreshToken(refreshToken).expire(7200).build();
    }


    @Override
    public TokenDTO refresh(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        if (!"refresh".equals(jwt.getClaimAsString("token_type"))) {
            throw new BadJwtException("Please use refresh token");
        }

        String scope = jwt.getClaimAsString("scope");
        String version = jwt.getClaimAsString("version");
        String subject = jwt.getSubject();

        return this.createToken(subject, scope, version);
    }
}
