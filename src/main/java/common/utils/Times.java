package common.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author valor
 */
public class Times {

    /**
     * 一天的秒数
     */
    public static final int second_per_day = 86400;

//    /**
//     * 默认时区
//     */
//    private static final ZoneId zoneId;
//    static {
//        zoneId = ZoneId.of("Asia/Shanghai");
//    }

    /**
     * 默认时区的时间偏移量 东八区 +8
     */
    private static final ZoneOffset zoneOffSet = ZoneOffset.ofTotalSeconds(28800);

    /**
     * 时间格式化 枚举类
     */
    public enum Formatter {
        DateTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US)),
        ISO_8601(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)),
        RFC_1123(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US));

        private final DateTimeFormatter formatter;

        Formatter(DateTimeFormatter formatter) {
            this.formatter = formatter;
        }

        public DateTimeFormatter getFormatter() {
            return this.formatter;
        }
    }

    /**
     * 不允许外部实例化
     */
    private Times() { }

    /**
     * 指定日期字符串 按指定格式转换成timestamp
     */
    public static long toTimestamp(String str, Formatter formatter) {
        return toTimestamp(str, formatter, zoneOffSet);
    }

    /**
     * 指定日期字符串 按指定格式转换成timestamp
     */
    public static long toTimestamp(String str, Formatter formatter, ZoneOffset offset) {
        return toDateTime(str, formatter).toEpochSecond(offset) * 1000L;
    }

    /**
     * 指定timestamp 按指定格式 格式化时间
     */
    public static String format(long time, Formatter formatter) {
        return format(time, formatter, zoneOffSet);
    }

    /**
     * 指定timestamp 按指定格式 格式化时间
     */
    public static String format(long time, Formatter formatter, ZoneOffset offset) {
        return format(toDateTime(time, offset), formatter);
    }

    /**
     * 指定LocalDateTime 按指定格式 格式化时间
     */
    public static String format(LocalDateTime time, Formatter formatter) {
        return time.format(formatter.getFormatter());
    }

    /**
     * 指定日期字符串 按指定格式转换成localDateTime
     */
    public static LocalDateTime toDateTime(String str, Formatter formatter) {
        return LocalDateTime.parse(str, formatter.getFormatter());
    }

    /**
     * 指定timestamp 转换成localDateTime
     */
    public static LocalDateTime toDateTime(long timestamp) {
        return toDateTime(timestamp, zoneOffSet);
    }

    /**
     * 指定timestamp 转换成localDateTime
     */
    public static LocalDateTime toDateTime(long timestamp, ZoneOffset offset) {
        long secs = timestamp / 1000L;
        return LocalDateTime.ofEpochSecond(secs, 0, offset);
    }

    /**
     * 判断指定timestamp 是否是过去的时间
     */
    public static boolean isPast(long time) {
        return System.currentTimeMillis() > time;
    }

    /**
     * 判断指定timestamp 是否是未来的时间
     */
    public static boolean isFuture(long time) {
        return System.currentTimeMillis() < time;
    }

    /**
     * 判断当前timestamp 是否在某个时间段内
     */
    public static boolean isInTime(long start, long end) {
        return isInTime(System.currentTimeMillis(), start, end);
    }

    /**
     * 判断指定timestamp 是否在某个时间段内
     */
    public static boolean isInTime(long time, long start, long end) {
        return start <= time && time < end;
    }

    /**
     * 计算两个日期相差的 毫秒数
     */
    public static long between(LocalDateTime one, LocalDateTime another) {
        return Duration.between(one, another).toMillis();
    }

    /**
     * 获取今日零时timestamp
     */
    public static long getZeroPoint() {
        return getZeroPoint(0);
    }

    /**
     * 指定与今日相差的天数 获取零时timestamp
     */
    public static long getZeroPoint(int days) {
        return getZeroPoint(days, zoneOffSet);
    }

    /**
     * 指定与今日相差的天数 获取零时timestamp
     */
    public static long getZeroPoint(int days, ZoneOffset zoneOffSet) {
        return getZeroPoint(System.currentTimeMillis(), days, zoneOffSet);
    }

    /**
     * 指定时间戳 获取零时timestamp
     */
    public static long getZeroPoint(long timestamp) {
        return getZeroPoint(timestamp, 0);
    }

    /**
     * 指定时间戳 获取零时timestamp
     */
    public static long getZeroPoint(long timestamp, int days) {
        return getZeroPoint(timestamp, days, zoneOffSet);
    }

    /**
     * 指定时间戳 获取零时timestamp
     */
    public static long getZeroPoint(long timestamp, int days, ZoneOffset zoneOffSet) {
        int offset = zoneOffSet.getTotalSeconds();

        long time = timestamp / 1000L + offset;
        long point = time - time % second_per_day;

        if (0 != days) {
            point += days * second_per_day;
        }

        // 计算时 传入的时区为标准
        return (point - offset) * 1000L;
    }

}
