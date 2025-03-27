package top.mxzero.common.utils;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

public class UUIDv7Generator {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    // 位掩码常量
    private static final long SUBSEC_A_MASK = 0x0FFF_0000_0000_0000L;
    private static final long SUBSEC_B_MASK = 0x0000_0000_0000_00FFL;
    private static final long VERSION_MASK = 0xF000L;
    private static final long VARIANT_MASK = 0xC000_0000_0000_0000L;

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
}