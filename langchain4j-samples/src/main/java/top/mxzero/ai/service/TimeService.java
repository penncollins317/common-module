package top.mxzero.ai.service;

import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import top.mxzero.common.utils.DateUtil;

import java.util.Date;

/**
 * @author Peng
 * @since 2025/4/24
 */
@Slf4j
public class TimeService {
    @Tool("获取当前系统时间的时间戳（毫秒）")
    public long currentTimestamp() {
        log.info("currentTimestamp called.");
        return System.currentTimeMillis();
    }

    @Tool("时间戳格式化为字符串，格式为：yyyy-MM-dd HH:mm:ss")
    public String formatDate(long timestamp) {
        log.info("formatDate called.");
        return DateUtil.formatDatetime(new Date(timestamp));
    }

}
