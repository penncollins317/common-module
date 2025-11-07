package top.echovoid.common.utils;

public class DecimalToBase36 {
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";  // 36 个字符

    public static String toBase36(long decimal) {
        if (decimal == 0) return "0";  // 特殊情况，零的处理

        StringBuilder base36 = new StringBuilder();

        while (decimal > 0) {
            int remainder = (int) (decimal % 36);  // 取余数，强制转换为 int 类型
            base36.insert(0, CHARS.charAt(remainder));  // 将字符插入到字符串的前端
            decimal /= 36;  // 更新十进制数
        }

        return base36.toString();
    }
}
