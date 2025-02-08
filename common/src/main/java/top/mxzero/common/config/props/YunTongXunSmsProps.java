package top.mxzero.common.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2023/9/10
 */
@Data
@ConfigurationProperties(prefix = "mxzero.sms.yuntongxun")
public class YunTongXunSmsProps {
    private String appid;
    private String accountSid;
    public String authToken;
    private String template;
    private String serverIp;
    private String serverPort;
    private boolean enable;
}