package top.mxzero.common.utils;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间格式化工具
 *
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2023/9/1
 */
public class DateUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter NUMBER_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    private static final DateTimeFormatter DATE_NUMBER_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateUtil() {
    }


    public static String formatDatetime(Date date) {
        return DATE_TIME_FORMATTER.format(date.toInstant().atZone(ZoneId.systemDefault()));
    }

    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date.toInstant().atZone(ZoneId.systemDefault()));
    }

    public static Date parseDate(String dateStr) {
        return Date.from(LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date parseDatetime(String datetimeStr) {
        return Date.from(LocalDateTime.from(DATE_TIME_FORMATTER.parse(datetimeStr)).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date fromIsoTimestamp(Long timestamp) {
        return new Date(timestamp * 1000);
    }

    public static String fromIsoTimestampToStr(Long timestamp) {
        return formatDatetime(new Date(timestamp * 1000));
    }

    public static String formatNumber(Date date) {
        return NUMBER_FORMATTER.format(date.toInstant().atZone(ZoneId.systemDefault()));
    }

    public static String formatDateNumber(Date date) {
        return DATE_NUMBER_FORMATTER.format(date.toInstant().atZone(ZoneId.systemDefault()));
    }

    public static boolean isDate(String dateStr) {
        return dateStr.matches("\\d{4}-\\d{2}-\\d{2}");
    }


    public static String beforeDateStr(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -day);
        return formatDate(calendar.getTime());
    }

    public static Date beforeDate(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -day);
        return calendar.getTime();
    }

    public static Date afterDate(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -day);
        return calendar.getTime();
    }

    public static Date afterDate(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    public static boolean isDateTime(String dateStr) {
        return dateStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
    }


    /**
     * 当前的日期字符串
     *
     * @return
     */
    public static String currentDayStr() {
        return formatDate(new Date());
    }

    /**
     * 两个日期排序，
     *
     * @return 返回两个日期数组
     */
    public static Date[] sort(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return new Date[]{date1, date2};
        }

        if (date1.getTime() == date2.getTime() || date1.getTime() > date2.getTime()) {
            return new Date[]{date1, date2};
        }

        return new Date[]{date2, date1};
    }

    /**
     * 两个日期排序，
     *
     * @return 返回两个日期数组
     */
    public static String[] sort(String date1, String date2) {
        if (!StringUtils.hasLength(date1) || !StringUtils.hasLength(date2)) {
            return new String[]{date1, date2};
        }

        Date date_1 = parseDate(date1);
        Date date_2 = parseDate(date2);

        if (date_1.getTime() == date_2.getTime() || date_1.getTime() > date_2.getTime()) {
            return new String[]{date1, date2};
        }

        return new String[]{date2, date1};
    }
}