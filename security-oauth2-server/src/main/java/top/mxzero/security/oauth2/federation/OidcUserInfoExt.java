package top.mxzero.security.oauth2.federation;

import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import top.mxzero.service.user.dto.UserinfoDTO;
import top.mxzero.service.user.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2024/8/15
 */
@AllArgsConstructor
public class OidcUserInfoExt implements Function<OidcUserInfoAuthenticationContext, OidcUserInfo> {
    private final UserService userService;

    @Override
    public OidcUserInfo apply(OidcUserInfoAuthenticationContext context) {
        Long userId = Long.parseLong(context.getAuthentication().getName());
        UserinfoDTO userinfo = this.userService.getUserinfo(userId);
        if (userinfo == null) {
            throw new IllegalStateException("user id not found");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", context.getAuthentication().getName());
        claims.put("id", userinfo.getId());
        claims.put("username", userinfo.getUsername());
        claims.put("avatar_url", userinfo.getAvatarUrl());
        return new OidcUserInfo(claims);
    }
}
