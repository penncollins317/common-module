package top.echovoid.oss.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Penn Collins
 * @since 2025/4/14
 */
@Getter
@AllArgsConstructor
public enum FileStatus {
    PENDING("pending"), // 上传中
    ACTIVE("active"),
    DELETED("deleted");

    @EnumValue
    @JsonValue
    private final String status;
}
