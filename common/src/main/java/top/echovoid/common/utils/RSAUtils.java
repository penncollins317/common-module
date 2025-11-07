package top.echovoid.common.utils;

import org.springframework.core.io.ClassPathResource;

import javax.crypto.Cipher;
import java.io.InputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author Peng
 * @email penncollins317@gmail.com
 * @since 2024/3/21
 */
public class RSAUtils {
    private static PrivateKey PRIVATE_KEY = null;
    private static PublicKey PUBLIC_KEY = null;

    static {
        try {
            try (InputStream inputStream = new ClassPathResource("META-INF/common_rsa_public_Key.txt").getInputStream()) {
                String publicKeyStr = new String(inputStream.readAllBytes());
                PUBLIC_KEY = converPublicKey(publicKeyStr);
            }
            try (InputStream inputStream = new ClassPathResource("META-INF/common_rsa_private_Key.txt").getInputStream()) {
                String privateKeyStr = new String(inputStream.readAllBytes());
                PRIVATE_KEY = converPrivateKey(privateKeyStr);
            }
        } catch (Exception e) {
            try {
                KeyPair keyPair = createKey();
                PRIVATE_KEY = keyPair.getPrivate();
                PUBLIC_KEY = keyPair.getPublic();
            } catch (NoSuchAlgorithmException e2) {
                throw new RuntimeException(e2);
            }
        }
    }

    private RSAUtils() {
    }

    public static String encodeByPrivateKey(String data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, PRIVATE_KEY);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encodeByPrivateKey(String data, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String encodeByPublicKey(String data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, PUBLIC_KEY);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decodeByPrivate(String data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, PRIVATE_KEY);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(data));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decodeByPublicKey(String data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, PUBLIC_KEY);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(data));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static KeyPair createKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // 指定密钥长度
        return keyPairGenerator.generateKeyPair();
    }

    public static String publicKeyToString(PublicKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    public static String privateKeyToString(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    // 将Base64编码的公钥字符串转换为PublicKey对象
    public static PublicKey converPublicKey(String publicKeyString) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    // 将Base64编码的私钥字符串转换为PrivateKey对象
    public static PrivateKey converPrivateKey(String privateKeyString) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    public static PublicKey getPublicKey() {
        return PUBLIC_KEY;
    }

    public static PrivateKey getPrivateKey() {
        return PRIVATE_KEY;
    }
}
