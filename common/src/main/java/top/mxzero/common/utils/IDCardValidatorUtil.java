package top.mxzero.common.utils;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2023/9/1
 */
public class IDCardValidatorUtil {
    // 正则表达式模式，用于匹配身份证号码
    private static final String ID_CARD_PATTERN =
            "^(\\d{15}$|^\\d{17}([0-9]|X|x))$";

    // 身份证号码正则表达式对象
    private static final Pattern pattern = Pattern.compile(ID_CARD_PATTERN);

    // 验证身份证号码的方法
    public static boolean validateIDCard(String idCard) {
        // 检查是否为空
        if (idCard == null || idCard.isEmpty()) {
            return false;
        }

        // 使用正则表达式匹配身份证号码
        Matcher matcher = pattern.matcher(idCard);
        if (!matcher.matches()) {
            return false;
        }

        // 验证出生日期
        if (!validateBirthDate(idCard)) {
            return false;
        }

        // 验证校验位（最后一位）
        return validateCheckBit(idCard);
    }

    // 验证出生日期的方法
    private static boolean validateBirthDate(String idCard) {
        try {
            int year, month, day;
            if (idCard.length() == 15) {
                year = Integer.parseInt("19" + idCard.substring(6, 8));
                month = Integer.parseInt(idCard.substring(8, 10));
                day = Integer.parseInt(idCard.substring(10, 12));
            } else {
                year = Integer.parseInt(idCard.substring(6, 10));
                month = Integer.parseInt(idCard.substring(10, 12));
                day = Integer.parseInt(idCard.substring(12, 14));
            }

            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH) + 1; // 月份从0开始
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

            if (year < 1900 || year > currentYear) {
                return false;
            }
            if (month < 1 || month > 12) {
                return false;
            }
            if (day < 1 || day > 31) {
                return false;
            }
            if (year == currentYear && month > currentMonth) {
                return false;
            }
            return year != currentYear || month != currentMonth || day <= currentDay;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // 验证校验位的方法
    private static boolean validateCheckBit(String idCard) {
        if (idCard.length() == 18) {
            // 18位身份证需要验证校验位
            char[] chars = idCard.toCharArray();
            int[] coefficients = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
            char[] checkBits = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

            int sum = 0;
            for (int i = 0; i < 17; i++) {
                int digit = Character.getNumericValue(chars[i]);
                sum += digit * coefficients[i];
            }

            int remainder = sum % 11;
            char expectedCheckBit = checkBits[remainder];
            return chars[17] == expectedCheckBit;
        }

        return true;
    }

    /**
     * 通过身份证号码获取出生日期字符串
     * 15位和18位身份证号均可获取
     * 身份证号格式错误返回null
     */
    public static String getBirthdayStr(String idCart) {
        if (validateIDCard(idCart)) {
            if (idCart.length() == 15) {
                String birthYear = "19" + idCart.substring(6, 8);
                String birthMonth = idCart.substring(8, 10);
                String birthDay = idCart.substring(10, 12);
                return birthYear + "-" + birthMonth + "-" + birthDay;
            }

            String birthYear = idCart.substring(6, 10);
            String birthMonth = idCart.substring(10, 12);
            String birthDay = idCart.substring(12, 14);
            return birthYear + "-" + birthMonth + "-" + birthDay;
        }

        return null;
    }

    /**
     * 根据身份证号码获取性别
     * 身份证格式错误返回null
     */
    public static String getGenderFromIDCard(String idCard) {
        if (!validateIDCard(idCard)) {
            return null;
        }

        char lastDigit = idCard.charAt(idCard.length() - 2); // 倒数第二位

        if (lastDigit % 2 == 0) {
            return "女"; // 偶数代表女性
        } else {
            return "男"; // 奇数代表男性
        }
    }

}
