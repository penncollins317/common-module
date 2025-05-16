package top.mxzero.security.auth.wechat;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Peng
 * @since 2025/5/17
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties(WechatBizAppProperties.class)
public class WechatQRCodeAuthAutoConfig {
}