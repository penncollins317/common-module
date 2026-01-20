package top.echovoid.security.auth.wechat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "微信公众号消息推送", description = "处理来自微信服务器的服务器校验、事件推送、消息解析等功能")
@Slf4j
@RestController
@RequestMapping("/api/wechat/biz/action")
@AllArgsConstructor
public class WechatBizPushActionController {
    private final WechatBizAppProperties wechatBizAppProperties;

    @Operation(summary = "微信服务器验证", description = "用于微信公众平台后台配置服务器 URL 时的合法性校验")
    @GetMapping
    public String wechatBizVerifyActionApi(
            @Parameter(description = "时间戳") @RequestParam("timestamp") String timestamp,
            @Parameter(description = "微信签名") @RequestParam("signature") String signature,
            @Parameter(description = "随机数") @RequestParam("nonce") String nonce,
            @Parameter(description = "随机字符串") @RequestParam("echostr") String echostr) {

        String[] res = new String[] { wechatBizAppProperties.getToken(), timestamp, nonce };
        Arrays.sort(res);
        String temp = String.join("", res);
        if (HashUtils.sha1(temp).equals(signature)) {
            return echostr;
        }
        return "";
    }

    @Operation(summary = "微信消息推送处理", description = "解析来自微信服务器的加密消息，支持事件推送、关键词自动回复等")
    @PostMapping
    public String wechatBizPushActionApi(@Parameter(description = "微信推送的加密 XML 报文") @RequestBody String body) {
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
