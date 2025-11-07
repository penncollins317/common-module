package top.echovoid.security.auth.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Peng
 * @since 2025/5/17
 */
@Data
@ConfigurationProperties("mxzero.security.wechat.biz")
public class WechatBizAppProperties {
    private String appid;
    private String appSecret;
    private String token;
    private String aesKey;
}