package top.echovoid.ai.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import top.echovoid.common.utils.DateUtil;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Penn Collins
 * @since 2025/3/19
 */
@Slf4j
@Component
public class DateTools {
    @Tool(description = "现在的时间是多少？")
    public String currentTime() {
        return DateUtil.formatDatetime(new Date());
    }

    @Tool(description = "根据时区ID（如'America/New_York'）查询当前时间。")
    public String currentTimeWithZone(@RequestParam(name = "zoneId") String zoneId) {
        // 校验时区合法性
        if (!ZoneId.getAvailableZoneIds().contains(zoneId)) {
            throw new IllegalArgumentException("无效的时区ID: " + zoneId);
        }
        ZoneId zone = ZoneId.of(zoneId);
        ZonedDateTime zonedTime = ZonedDateTime.now(zone);
        String formattedTime = zonedTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
        log.info("调用 currentTimeWithZone 方法，时区: {}, 时间: {}", zoneId, formattedTime);
        return formattedTime;
    }
}

