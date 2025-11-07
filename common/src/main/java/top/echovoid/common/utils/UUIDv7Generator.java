package top.echovoid.common.utils;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

/**
 * UUID v7生成器
 *
 * @author Peng
 * @email penncollins317@gmail.com
 * @since 2025/3/19
 */
public class UUIDv7Generator {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generateStr() {
        return generate().toString();
    }

    public static UUID generate() {
        // 获取当前时间戳（毫秒精度 + 子秒精度）
        Instant now = Instant.now();
        long tsMillis = now.toEpochMilli();
        int subsecNanos = now.getNano(); // 0-999,999,999

        // 拆分时间戳部分
        long subsecA = (subsecNanos >>> 20) & 0x0FFF; // 高12位
        long subsecB = (subsecNanos >>> 12) & 0x00FF; // 低8位 (取中间部分)

        // 生成54位随机数
        long rand = SECURE_RANDOM.nextLong() & 0x003F_FFFF_FFFF_FFFFL;

        // 组合各部分数据
        long msb = ((tsMillis & 0xFFFF_FFFF_FFFFL) << 16)  // 48位时间戳
                | (0x7000L | subsecA);                    // 版本号7 + 子秒高

        long lsb = (0x8000_0000_0000_0000L)                // 变体位 (RFC4122)
                | ((subsecB & 0xFFL) << 54)               // 子秒低
                | rand;                                    // 54位随机数

        return new UUID(msb, lsb);
    }

    public static void validate(String uuidV7) {
        validate(UUID.fromString(uuidV7));
    }

    // 验证方法（可选）
    public static void validate(UUID uuid) {
        if (uuid.version() != 7) {
            throw new IllegalArgumentException("Not a UUIDv7");
        }
        if (uuid.variant() != 2) {
            throw new IllegalArgumentException("Invalid RFC4122 variant");
        }
    }

    /**
     * 从 UUIDv7 中提取秒级别的时间戳
     * <p>
     * 该方法只恢复存储在 UUID 高 48 位中的毫秒时间戳，
     * 并将其转换为秒级别（忽略毫秒及子秒部分）。
     *
     * @param uuid UUIDv7 对象
     * @return 时间戳的秒级值
     */
    public static long extractTimestampSeconds(UUID uuid) {
        // 验证 UUID 是否为 UUIDv7
        validate(uuid);

        // 从 msb 的高 48 位恢复毫秒时间戳
        long tsMillis = uuid.getMostSignificantBits() >>> 16;
        return tsMillis / 1000;
    }

    /**
     * 从 UUIDv7 字符串中提取秒级别的时间戳
     *
     * @param uuidV7 UUIDv7 的字符串表示
     * @return 时间戳的秒级值
     */
    public static long extractTimestampSeconds(String uuidV7) {
        return extractTimestampSeconds(UUID.fromString(uuidV7));
    }
}