package top.echovoid.common.utils;

/**
 * 邮箱验证以及脱敏
 *
 * @author Peng
 * @email penncollins317@gmail.com
 * @since 2023/9/1
 */
public class EmailUtil {
    private EmailUtil() {
    }


    public static boolean isEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches("[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+");
    }

    public static String maskEmail(String email) {
        // 不符邮箱，直接返回
        if (!isEmail(email)) {
            return email;
        }

        String[] parts = email.split("@");
        if (parts.length != 2) {
            return email; // 不是合法的邮箱格式，返回原始邮箱
        }

        String username = parts[0];
        String domain = parts[1];

        int visibleChars = Math.min(2, username.length()); // 显示的字符数
        String maskedUsername = username.substring(0, visibleChars) + repeatChar(username.length() - visibleChars);

        return maskedUsername + "@" + domain;
    }

    private static String repeatChar(int times) {
        return "*".repeat(Math.max(0, times));
    }

}
