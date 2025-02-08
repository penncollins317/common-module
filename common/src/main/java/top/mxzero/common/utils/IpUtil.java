package top.mxzero.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取客户端IP地址
 *
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2023/9/1
 */
public class IpUtil {
    private static final String DEFAULT_NONE = "none";

    private static final Pattern pattern = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

    private static final Pattern IPV6_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

    private IpUtil() {
    }


    /**
     * 验证输入的IPv4地址是否正确
     */
    public static boolean validateIP(String ipAddress) {
        if (ipAddress == null || ipAddress.isEmpty()) {
            return false;
        }

        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    /**
     * 验证IPv6地址是否正确
     */
    public static boolean validateIPv6(String ipv6Address) {
        if (ipv6Address == null || ipv6Address.isEmpty()) {
            return false;
        }

        Matcher matcher = IPV6_PATTERN.matcher(ipv6Address);
        return matcher.matches();
    }

    /**
     * 获取请求客户端IP地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        if (request == null) {
            return DEFAULT_NONE;
        }
        String ip;
        ip = request.getHeader("X-Original-Forwarded-For");

        if (!StringUtils.hasLength(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }

        if (!StringUtils.hasLength(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (!StringUtils.hasLength(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    public static String getRemoteAddr() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return DEFAULT_NONE;
        }
        return getRemoteAddr(requestAttributes.getRequest());
    }

    public static boolean isIPInRange(String ipAddress, String startRange, String endRange) {
        try {
            InetAddress ip = InetAddress.getByName(ipAddress);
            InetAddress start = InetAddress.getByName(startRange);
            InetAddress end = InetAddress.getByName(endRange);

            // 将InetAddress转换为整数进行比较
            long ipLong = ipToLong(ip);
            long startLong = ipToLong(start);
            long endLong = ipToLong(end);

            return ipLong >= startLong && ipLong <= endLong;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    public static long ipToLong(InetAddress ipAddress) {
        byte[] octets = ipAddress.getAddress();
        long result = 0;
        for (byte octet : octets) {
            result <<= 8;
            result |= octet & 0xff;
        }
        return result;
    }

    /**
     * 判断IP是否是内网IP
     *
     * @param ipAddress IPv4
     * @return
     */
    public static boolean isPrivateIPAddress(String ipAddress) {

        if (validateIPv6(ipAddress)) {
            return ipAddress.startsWith("fc00:") || ipAddress.startsWith("fe80:") || ipAddress.startsWith("0:0:0");
        }

        String[] parts = ipAddress.split("\\.");
        if (parts.length != 4)
            return false;

        int firstByte = Integer.parseInt(parts[0]);
        int secondByte = Integer.parseInt(parts[1]);

        if (firstByte == 10 ||
                (firstByte == 172 && secondByte >= 16 && secondByte <= 31) ||
                (firstByte == 192 && secondByte == 168) ||
                firstByte == 127) {
            return true;
        }

        return false;
    }
}
