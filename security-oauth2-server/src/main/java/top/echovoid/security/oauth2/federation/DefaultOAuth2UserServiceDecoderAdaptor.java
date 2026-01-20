package top.echovoid.security.oauth2.federation;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import top.echovoid.common.utils.JsonUtils;
import top.echovoid.security.oauth2.entity.OAuth2UserRelated;
import top.echovoid.security.oauth2.mapper.OAuth2UserRelatedMapper;
import top.echovoid.service.user.entity.User;
import top.echovoid.service.user.mapper.UserMapper;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Penn Collins
 * @since 2025/3/31
 */
@Slf4j
public class DefaultOAuth2UserServiceDecoderAdaptor extends DefaultOAuth2UserService {
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    private final OAuth2UserRelatedMapper oAuth2UserRelatedMapper;
    private final UserMapper userMapper;

    public DefaultOAuth2UserServiceDecoderAdaptor(OAuth2UserRelatedMapper mapper, UserMapper userMapper) {
        this.oAuth2UserRelatedMapper = mapper;
        this.userMapper = userMapper;

    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        if ("qq".equals(request.getClientRegistration().getRegistrationId())) {
            return this.loadUserHandlerQQ(request);
        } else if ("weibo".equals(request.getClientRegistration().getRegistrationId())) {
            return this.loadUserHandlerWeibo(request);
        }
        OAuth2User oAuth2User = super.loadUser(request);
        return wrapperOAuthUser(request.getClientRegistration().getClientName(), oAuth2User.getName(), new HashMap<>(oAuth2User.getAttributes()));
    }

    private OAuth2User loadUserHandlerWeibo(OAuth2UserRequest request) {
        String uid = this.getUid(request.getAccessToken().getTokenValue());
        URI uri = UriComponentsBuilder.fromUriString(request.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri()).queryParam("access_token", request.getAccessToken().getTokenValue()).queryParam("uid", uid).build().encode() // 重要！避免特殊字符问题
                .toUri();
        try {
            ResponseEntity<String> response = REST_TEMPLATE.getForEntity(uri, String.class);
            Map<String, Object> map = JsonUtils.parseMap(response.getBody());
            return this.wrapperOAuthUser(request.getClientRegistration().getClientName(), map.get(request.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName()).toString(), map);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new OAuth2AuthenticationException("登录失败");
        }
    }

    private String getUid(String accessToken) {
        URI uri = UriComponentsBuilder.fromUriString("https://api.weibo.com/2/account/get_uid.json").queryParam("access_token", accessToken).build().encode() // 重要！避免特殊字符问题
                .toUri();

        try {
            ResponseEntity<String> response = REST_TEMPLATE.getForEntity(uri, String.class);
            Map<String, Object> map = JsonUtils.parseMap(response.getBody());
            return map.get("uid").toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new OAuth2AuthenticationException("登录失败");
        }
    }


    private OAuth2User loadUserHandlerQQ(OAuth2UserRequest request) {
        URI uri = UriComponentsBuilder.fromUriString("https://graph.qq.com/oauth2.0/me").queryParam("access_token", request.getAccessToken().getTokenValue()).queryParam("fmt", "json").build().encode() // 重要！避免特殊字符问题
                .toUri();

        try {
            ResponseEntity<String> response = REST_TEMPLATE.getForEntity(uri, String.class);
            Map<String, Object> map = JsonUtils.parseMap(response.getBody());
            String openid = (String) map.get("openid");
            Map<String, Object> userinfo = this.getUserinfo(request.getClientRegistration().getClientId(), request.getAccessToken().getTokenValue(), openid);
            userinfo.put("openid", openid);
            return this.wrapperOAuthUser(request.getClientRegistration().getClientName(), openid, userinfo);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new OAuth2AuthenticationException("登录失败");
        }
    }

    private Map<String, Object> getUserinfo(String appid, String accessToken, String openid) {
        URI uri = UriComponentsBuilder.fromUriString("https://graph.qq.com/user/get_user_info").queryParam("access_token", accessToken).queryParam("openid", openid).queryParam("oauth_consumer_key", appid).build().encode() // 重要！避免特殊字符问题
                .toUri();

        ResponseEntity<String> response = REST_TEMPLATE.getForEntity(uri, String.class);
        return JsonUtils.parseMap(response.getBody());
    }

    public OAuth2User wrapperOAuthUser(String provider, String uid, Map<String, Object> attrs) {
        OAuth2UserRelated related = this.oAuth2UserRelatedMapper.selectOne(new QueryWrapper<OAuth2UserRelated>().eq("provider", provider).eq("out_uid", uid));
        if (related == null) {
            User user = new User();
            user.setUsername("u_" + IdWorker.getId());
            user.setNickname(user.getUsername());
            this.userMapper.insert(user);

            related = new OAuth2UserRelated();
            related.setOutUid(uid);
            related.setProvider(provider);
            related.setUserId(user.getId());
            this.oAuth2UserRelatedMapper.insert(related);
        }

        attrs.put("_system_uid", related.getUserId().toString());
        return new DefaultOAuth2User(List.of(), attrs, "_system_uid");
    }
}
