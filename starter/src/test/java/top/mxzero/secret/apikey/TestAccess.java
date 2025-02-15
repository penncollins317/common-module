package top.mxzero.secret.apikey;

import org.junit.jupiter.api.Test;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import top.mxzero.security.apikeys.utils.SignatureUtil;

public class TestAccess {
    private final String appid = "1890677361747415041";
    private final String secretKey = "F0JRQzkHaPVyW3cJ4bBKvQuT02BPcNgUBxGpJfpEwJlqVZQcGeUKvkiuOwew5VM8";
    private final String targetUrl = "http://localhost:8080/openapi/test";

    @Test
    public void testAccessOpenApi() throws Exception {
        // 构建请求体
        Map<String, Object> requestBody = Map.of("name", "张三", "age", 20);

        // 生成签名、时间戳、随机数（nonce）
        String[] result = SignatureUtil.generateSignature(Map.of(), requestBody, secretKey);
        String signature = result[0];
        String timestamp = result[1];
        String nonce = result[2];

        // 构建请求头
        Map<String, String> headers = Map.of(
                "x-appid", appid,
                "x-signature", signature,
                "x-timestamp", timestamp,
                "x-nonce", nonce
        );

        // 发送 POST 请求
        sendPostRequest(targetUrl, requestBody, headers);
    }

    /**
     * 发送 POST 请求
     */
    private void sendPostRequest(String targetUrl, Map<String, Object> requestBody, Map<String, String> headers) throws Exception {
        // 创建 HttpClient 实例
        HttpClient client = HttpClient.newHttpClient();

        // 将请求体转为 JSON 字符串
        String jsonRequestBody = new ObjectMapper().writeValueAsString(requestBody);

        // 构建 HttpRequest 请求
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(targetUrl))
                .header("Content-Type", "application/json");  // 设置请求体类型为 JSON

        // 添加请求头
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.header(entry.getKey(), entry.getValue());
        }

        // 设置 POST 请求方法和请求体
        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody))
                .build();

        // 发送请求并获取响应
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 打印响应
        System.out.println("Response status code: " + response.statusCode());
        System.out.println("Response body: " + response.body());
    }
}
