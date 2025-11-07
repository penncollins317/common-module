package top.echovoid.common.service.impl;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import top.echovoid.common.config.props.YunTongXunSmsProps;
import top.echovoid.common.service.SmsSender;

import java.util.HashMap;

/**
 * @author Penn Collins
 * @email penncollins317@gmail.com
 * @since 2024/1/22
 */
@Slf4j
public class YunTongXinSmsSender implements SmsSender {
    private final YunTongXunSmsProps props;
    private CCPRestSmsSDK smsClient;

    public YunTongXinSmsSender(YunTongXunSmsProps props) {
        this.props = props;

        try {
            this.smsClient = new CCPRestSmsSDK();
            this.smsClient.init(props.getServerIp(), props.getServerPort());
            this.smsClient.setAccount(props.getAccountSid(), props.getAuthToken());
            this.smsClient.setAppId(props.getAppid());
            this.smsClient.setBodyType(BodyType.Type_JSON);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 调用容联云短信验证码接口
     *
     * @param phone 手机号码
     * @param code  验证码
     */
    @Async
    public void sendSmsAction(String phone, String code) {
        String[] data = {code, "30分钟"};
        HashMap<String, Object> response = this.smsClient.sendTemplateSMS(phone, props.getTemplate(), data);
        boolean isSuccess = "000000".equals(response.get("statusCode"));
        if (!isSuccess) {
            log.error("短信发送失败：{}, {}:{}", phone, response.get("statusCode"), response.get("statusMsg"));
        }
    }
}
