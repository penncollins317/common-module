package top.echovoid.payment.alipay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Penn Collins
 * @since 2025/10/1
 */
@Data
@ConfigurationProperties(prefix = "echovoid.alipay")
public class AliPayConfigProps {
    private String gatewayUrl;
    private String appid;
    private String notifyUrl;
    private String appPrivateKeyPath;
    private String alipayPublicKey;
}
