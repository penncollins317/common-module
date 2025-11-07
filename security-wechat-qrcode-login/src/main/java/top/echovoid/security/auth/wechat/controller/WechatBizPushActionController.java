package top.echovoid.security.auth.wechat.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.echovoid.common.utils.HashUtils;
import top.echovoid.common.utils.WechatAesUtils;
import top.echovoid.common.utils.XmlToMapUtil;
import top.echovoid.security.auth.wechat.WechatBizAppProperties;

import java.util.Arrays;
import java.util.Map;

/**
 * 微信公众号消息推送
 *
 * @author Penn Collins
 * @since 2025/5/17
 */
@Slf4j
@RestController
@RequestMapping("/api/wechat/biz/action")
@AllArgsConstructor
public class WechatBizPushActionController {
    private final WechatBizAppProperties wechatBizAppProperties;

    @GetMapping
    public String wechatBizVerifyActionApi(
            @RequestParam("timestamp") String timestamp,
            @RequestParam("signature") String signature,
            @RequestParam("nonce") String nonce,
            @RequestParam("echostr") String echostr
    ) {

        String[] res = new String[]{wechatBizAppProperties.getToken(), timestamp, nonce};
        Arrays.sort(res);
        String temp = String.join("", res);
        if (HashUtils.sha1(temp).equals(signature)) {
            return echostr;
        }
        return "";
    }

    @PostMapping
    public String wechatBizPushActionApi(@RequestBody String body) {
        try {
            Map<String, String> bodyMap = XmlToMapUtil.xmlToMap(body);
            String decrypt = WechatAesUtils.decrypt(bodyMap.get("Encrypt"), wechatBizAppProperties.getAesKey());
            Map<String, String> data = XmlToMapUtil.xmlToMap(decrypt);
            log.info("{}", data);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "";
    }

}
