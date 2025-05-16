package top.mxzero.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Base64;

/**
 * 微信AES工具类
 *
 * @author Peng
 * @since 2025/5/17
 */
public class WechatAesUtils {
    /**
     * AES 解密
     *
     * @param encryptedBase64 密文（Base64 编码）
     * @param encodingEesKey  密钥（43个字符）
     * @return 明文字符串
     */
    public static String decrypt(String encryptedBase64, String encodingEesKey) throws Exception {
        // 1. Base64 解码 AES Key
        byte[] aesKey = Base64.getDecoder().decode(encodingEesKey + "="); // 必须加上 "=" 补足 base64

        // 2. 解码密文
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedBase64);

        // 3. 初始化 Cipher（AES-256-CBC，PKCS7Padding）
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
        IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

        // 4. 执行解密
        byte[] original = cipher.doFinal(encryptedBytes);

        // 5. 去除 PKCS7 填充
        byte[] bytes = decodePKCS7Padding(original);

        // 6. 按照微信格式解析明文
        //    [16字节随机] + [4字节消息长度] + [xml字符串] + [appid]
        int xmlLength = recoverNetworkBytesOrder(Arrays.copyOfRange(bytes, 16, 20));
        String xml = new String(Arrays.copyOfRange(bytes, 20, 20 + xmlLength), "UTF-8");

        return xml;
    }

    private static byte[] decodePKCS7Padding(byte[] decrypted) {
        int pad = decrypted[decrypted.length - 1];
        if (pad < 1 || pad > 32) pad = 0;
        return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
    }

    private static int recoverNetworkBytesOrder(byte[] orderBytes) {
        int sourceNumber = 0;
        for (int i = 0; i < 4; i++) {
            sourceNumber <<= 8;
            sourceNumber |= orderBytes[i] & 0xff;
        }
        return sourceNumber;
    }
}
