package top.mxzero.security.apikeys.utils;

import java.security.SecureRandom;

public class SecretKeyUtil {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private SecretKeyUtil() {
    }

    /**
     * 生成一个指定字符长的随机密钥
     */
    public static String generateSecretKey(int keyLength) {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder secretKey = new StringBuilder(keyLength);

        for (int i = 0; i < keyLength; i++) {
            int randomIndex = secureRandom.nextInt(CHARACTERS.length());
            secretKey.append(CHARACTERS.charAt(randomIndex));
        }

        return secretKey.toString();
    }
}
