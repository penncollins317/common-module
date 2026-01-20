package top.echovoid.security.oauth2.authentication;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import top.echovoid.service.user.dto.UserinfoDTO;
import top.echovoid.service.user.entity.User;
import top.echovoid.service.user.mapper.UserMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Penn Collins
 * @since 2025/3/19
 */
public class InterUserOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private static final String INTERNAL_USER_ID_KEY = "_internal_user_id";
    @Autowired
    private UserMapper userMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 获取第三方平台 ID
        Map<String, Object> attributes = oAuth2User.getAttributes(); // 获取第三方用户信息

        // 提取第三方用户的唯一标识（如 OpenID 或 UID）
        String oauth2UserId = null;
        if ("github".equals(registrationId)) {
            oauth2UserId = (String) attributes.get("sub");
        } else if ("qq".equals(registrationId)) {
            oauth2UserId = (String) attributes.get("openid");
        } else {
            throw new OAuth2AuthenticationException("Unsupported OAuth2 provider");
        }
        this.loadInternalUser(oauth2UserId, registrationId);
        // 检查是否已绑定
//        User systemUser = userService.findByOauth2UserId(registrationId, oauth2UserId);

//        if (systemUser == null) {
//            // 如果未绑定，则创建或绑定系统用户
//            systemUser = registerNewUser(userRequest, oAuth2User);
//        }
        Map<String, Object> attr = new HashMap<>(oAuth2User.getAttributes());
        attr.put(INTERNAL_USER_ID_KEY, "1");
        return new DefaultOAuth2User(oAuth2User.getAuthorities(), attr, INTERNAL_USER_ID_KEY);
    }

    private Long loadInternalUser(String oauth2UserId, String provider) {
        return 0l;
    }

    private UserinfoDTO registerNewUser(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        // 创建系统用户并绑定第三方用户
        String username;
        do {
            username = "u_" + IdWorker.getId();
        } while (this.userMapper.exists(new QueryWrapper<User>().eq("username", username)));

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setNickname((String) attributes.get("name"));
        newUser.setEmail((String) attributes.get("email"));
        userMapper.insert(newUser);
        return userMapper.findUserinfoById(newUser.getId());
    }
}
