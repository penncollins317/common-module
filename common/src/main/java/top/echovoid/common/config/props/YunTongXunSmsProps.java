package top.echovoid.common.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Penn Collins
 * @email penncollins317@gmail.com
 * @since 2023/9/10
 */
@Data
@ConfigurationProperties(prefix = "echovoid.sms.yuntongxun")
public class YunTongXunSmsProps {
    private String appid;
    private String accountSid;
    public String authToken;
    private String template;
    private String serverIp;
    private String serverPort;
    private boolean enable;
}