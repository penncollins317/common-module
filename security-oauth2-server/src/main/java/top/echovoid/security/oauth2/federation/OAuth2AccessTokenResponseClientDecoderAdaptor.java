package top.echovoid.security.oauth2.federation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import top.echovoid.common.utils.JsonUtils;

import java.net.URI;
import java.util.Map;

/**
 * 适配QQ、微博获取access_token接口
 *
 * @author Penn Collins
 * @since 2025/3/31
 */
@Slf4j
public class OAuth2AccessTokenResponseClientDecoderAdaptor implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    private static final DefaultAuthorizationCodeTokenResponseClient DEFAULT_AUTHORIZATION_CODE_TOKEN_RESPONSE_CLIENT = new DefaultAuthorizationCodeTokenResponseClient();

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest request) {
        if ("qq".equals(request.getClientRegistration().getRegistrationId())) {
            return this.handlerByQQ(request);
        } else if ("weibo".equals(request.getClientRegistration().getRegistrationId())) {
            return this.handlerByWeibo(request);
        }
        return DEFAULT_AUTHORIZATION_CODE_TOKEN_RESPONSE_CLIENT.getTokenResponse(request);
    }

    public OAuth2AccessTokenResponse handlerByWeibo(OAuth2AuthorizationCodeGrantRequest request) {
        URI uri = UriComponentsBuilder.fromHttpUrl(request.getClientRegistration().getProviderDetails().getTokenUri())
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", request.getClientRegistration().getClientId())
                .queryParam("client_secret", request.getClientRegistration().getClientSecret())
                .queryParam("code", request.getAuthorizationExchange().getAuthorizationResponse().getCode())
                .queryParam("redirect_uri", request.getClientRegistration().getRedirectUri())
                .queryParam("fmt", "json")
                .build()
                .encode() // 重要！避免特殊字符问题
                .toUri();

        try {
            ResponseEntity<String> response = REST_TEMPLATE.postForEntity(uri, null, String.class);
            Map<String, Object> map = JsonUtils.parseMap(response.getBody());
            return OAuth2AccessTokenResponse.withToken((String) map.get("access_token"))
                    .expiresIn(Long.parseLong((map.get("expires_in").toString())))
                    .tokenType(OAuth2AccessToken.TokenType.BEARER)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new OAuth2AuthenticationException("登录失败");
        }
    }

    public OAuth2AccessTokenResponse handlerByQQ(OAuth2AuthorizationCodeGrantRequest request) {
        URI uri = UriComponentsBuilder.fromHttpUrl(request.getClientRegistration().getProviderDetails().getTokenUri())
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", request.getClientRegistration().getClientId())
                .queryParam("client_secret", request.getClientRegistration().getClientSecret())
                .queryParam("code", request.getAuthorizationExchange().getAuthorizationResponse().getCode())
                .queryParam("redirect_uri", request.getClientRegistration().getRedirectUri())
                .queryParam("fmt", "json")
                .build()
                .encode() // 重要！避免特殊字符问题
                .toUri();

        try {
            ResponseEntity<String> response = REST_TEMPLATE.getForEntity(uri, String.class);
            Map<String, Object> map = JsonUtils.parseMap(response.getBody());
            return OAuth2AccessTokenResponse.withToken((String) map.get("access_token"))
                    .refreshToken((String) map.get("refresh_token"))
                    .expiresIn(Long.parseLong((map.get("expires_in").toString())))
                    .tokenType(OAuth2AccessToken.TokenType.BEARER)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
