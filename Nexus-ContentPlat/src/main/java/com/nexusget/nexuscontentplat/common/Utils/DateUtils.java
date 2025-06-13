package com.nexusget.nexuscontentplat.common.Utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 博客系统时间工具类（线程安全）
 */
public class DateUtils {

    //=== 常用格式化器（全部线程安全）===//
    private static final DateTimeFormatter DEFAULT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_ONLY_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter ISO8601_FORMATTER =
            DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    //=== 核心方法 ===//

    /**
     * 格式化当前时间（默认格式）
     */
    public static String now() {
        return format(LocalDateTime.now());
    }

    /**
     * 格式化LocalDateTime -> 字符串（默认格式）
     */
    public static String format(LocalDateTime dateTime) {
        return dateTime.format(DEFAULT_FORMATTER);
    }

    /**
     * 格式化时间戳 -> 字符串（默认格式）
     */
    public static String format(long timestamp) {
        return format(millisToLocalDateTime(timestamp));
    }

    /**
     * 字符串解析 -> LocalDateTime（默认格式）
     */
    public static LocalDateTime parse(String dateStr) {
        return LocalDateTime.parse(dateStr, DEFAULT_FORMATTER);
    }

    //=== 前端友好格式 ===//

    /**
     * 转换为ISO8601格式（推荐API返回使用）
     * 示例：2023-08-20T12:34:56+08:00
     */
    public static String toISOString(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault())
                .format(ISO8601_FORMATTER);
    }

    /**
     * 仅返回日期部分（用于生日等场景）
     */
    public static String dateOnly(LocalDateTime dateTime) {
        return dateTime.format(DATE_ONLY_FORMATTER);
    }

    //=== 时间计算 ===//

    /**
     * 计算时间差（人性化显示）
     * 示例：3分钟前、2小时前、昨天
     */
    public static String humanReadable(LocalDateTime dateTime) {
        Duration duration = Duration.between(dateTime, LocalDateTime.now());

        if (duration.toMinutes() < 1) {
            return "刚刚";
        } else if (duration.toHours() < 1) {
            return duration.toMinutes() + "分钟前";
        } else if (duration.toDays() < 1) {
            return duration.toHours() + "小时前";
        } else if (duration.toDays() == 1) {
            return "昨天";
        } else {
            return dateOnly(dateTime);
        }
    }

    //=== 类型转换 ===//

    /**
     * 时间戳 -> LocalDateTime
     */
    public static LocalDateTime millisToLocalDateTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * LocalDateTime -> 时间戳
     */
    public static long toTimestamp(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    /**
     * 兼容旧版Date转换
     */
    public static Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    //=== 缓存相关 ===//

    /**
     * 获取Redis过期时间（当前时间+时长）
     */
    public static long getRedisExpire(long duration, TimeUnit unit) {
        return System.currentTimeMillis() + unit.toMillis(duration);
    }
}