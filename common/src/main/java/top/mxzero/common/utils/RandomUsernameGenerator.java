package top.mxzero.common.utils;

import java.security.SecureRandom;

public class RandomUsernameGenerator {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomUsername(int length) {
        StringBuilder username = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            username.append(CHARACTERS.charAt(index));
        }
        return username.toString();
    }
}