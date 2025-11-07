package top.echovoid.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class SignatureUtil {

    /**
     * 生成请求签名
     *
     * @param queryParams 请求参数 (Map)
     * @param body        请求体 (Map)
     * @param secretKey   用于生成签名的密钥 (String)
     * @return 签名字符串 (String)
     */
    public static String[] generateSignature(Map<String, String> queryParams, Map<String, Object> body, String secretKey) throws Exception {
        // 获取当前时间戳（以秒为单位）
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);  // 秒级时间戳

        // 将请求参数按键排序并拼接
        Map<String, String> sortedQueryParams = new TreeMap<>(queryParams);
        StringBuilder sortedQueryString = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedQueryParams.entrySet()) {
            if (sortedQueryString.length() > 0) {
                sortedQueryString.append("&");
            }
            sortedQueryString.append(entry.getKey()).append("=").append(entry.getValue());
        }

        // 将请求体转换为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String bodyString = objectMapper.writeValueAsString(body);

        // 生成随机字符串（nonce），这里使用 UUID
        String nonce = UUID.randomUUID().toString();

        // 拼接待签名字符串
        String stringToSign = sortedQueryString.toString() + "\n" + bodyString + "\n" + timestamp + "\n" + nonce;
        System.out.println(stringToSign);
        // 使用 HMAC-SHA256 算法生成签名
        String signature = hmacSHA256(stringToSign, secretKey);

        // 返回签名、时间戳和随机字符串
        return new String[]{signature, timestamp, nonce};
    }

    /**
     * 使用 HMAC-SHA256 算法生成签名
     *
     * @param data 待签名数据 (String)
     * @param key  用于签名的密钥 (String)
     * @return 签名 (String)
     */
    private static String hmacSHA256(String data, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    /**
     * 验证请求签名
     *
     * @param queryParams       请求参数 (Map)
     * @param body              请求体 (Map)
     * @param secretKey         用于生成签名的密钥 (String)
     * @param providedSignature 提供的签名 (String)
     * @param timestamp         时间戳 (String)
     * @param nonce             随机字符串 (String)
     * @return 是否验证成功 (boolean)
     */
    public static boolean validateSignature(Map<String, String> queryParams, Map<String, Object> body, String secretKey, String timestamp, String nonce, String providedSignature) {
        // 将请求参数按键排序并拼接
        Map<String, String> sortedQueryParams = new TreeMap<>(queryParams);
        StringBuilder sortedQueryString = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedQueryParams.entrySet()) {
            if (sortedQueryString.length() > 0) {
                sortedQueryString.append("&");
            }
            sortedQueryString.append(entry.getKey()).append("=").append(entry.getValue());
        }

        // 将请求体转换为 JSON 字符串
        String bodyString = JsonUtils.stringify(new TreeMap<>(body));

        // 拼接待验证签名的字符串
        String stringToSign = sortedQueryString + "\n" + bodyString + "\n" + timestamp + "\n" + nonce;
        System.out.println(stringToSign);
        try {
            // 计算签名, 比较提供的签名和生成的签名
            return hmacSHA256(stringToSign, secretKey).equals(providedSignature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

