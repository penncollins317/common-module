package top.echovoid.tts.translate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.echovoid.tts.translate.config.TTSProperties;
import top.echovoid.tts.translate.service.TTSService;
import top.echovoid.tts.translate.utils.AuthV3Util;
import top.echovoid.tts.translate.utils.HttpUtil;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 有道智云对接
 *
 * @author Peng
 * @since 2025/4/23
 */
@Slf4j
@AllArgsConstructor
public class YouDaoAiTTSService implements TTSService {
    private static final String VOICE_NAME = "youxiaoxun";
    private static final String FORMAT = "mp3";
    private static final String VOLUME = "5.00";
    private static final String SERVICE_URL = "https://openapi.youdao.com/ttsapi";
    private final TTSProperties properties;

    @Override
    public byte[] translateByBytes(String text) {
        Map<String, String[]> params = createRequestParams(text);
        try {
            AuthV3Util.addAuthParams(properties.getAppKey(), properties.getAppSecret(), params);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        log.info("text to audio:{}", text);

        return HttpUtil.doPost(SERVICE_URL, null, params, "audio");
    }

    @Override
    public String translateByBase64(String text) {
        return "";
    }

    @Override
    public String translateByAccessUrl(String text) {
        return "";
    }

    private static Map<String, String[]> createRequestParams(String text) {
        return new HashMap<>() {{
            put("q", new String[]{text});
            put("voiceName", new String[]{VOICE_NAME});
            put("format", new String[]{FORMAT});
            put("volume", new String[]{VOLUME});
        }};
    }
}
