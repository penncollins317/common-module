package top.mxzero.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class HashUtils {

    public static String md5(String input) {
        return md5(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String md5(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input);
            StringBuilder hexString = new StringBuilder();

            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String sha256(byte[] input) {
        try {
            // 创建 SHA-1 MessageDigest 实例
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            // 执行加密
            byte[] hashedBytes = messageDigest.digest(input);

            // 将字节数组转换为十六进制表示的字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);  // 转为二进制的十六进制字符串
                if (hex.length() == 1) {
                    hexString.append('0');  // 补充0到单个字符
                }
                hexString.append(hex);
            }

            return hexString.toString();  // 返回加密后的字符串
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String sha256(String input) {
        return sha256(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String sha1(String input) {
        try {
            // 创建 SHA-1 MessageDigest 实例
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

            // 执行加密
            byte[] hashedBytes = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));

            // 将字节数组转换为十六进制表示的字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);  // 转为二进制的十六进制字符串
                if (hex.length() == 1) {
                    hexString.append('0');  // 补充0到单个字符
                }
                hexString.append(hex);
            }

            return hexString.toString();  // 返回加密后的字符串
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
