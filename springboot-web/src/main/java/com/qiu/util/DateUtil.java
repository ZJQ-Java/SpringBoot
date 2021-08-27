package com.qiu.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtil {

    /**
     * 日期格式：yyyy-MM-dd
     */
    public final static String            NORM_DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 日期格式：yyyy-MM-dd
     */
    public final static DateTimeFormatter NORM_DATE_FORMAT  = DateTimeFormatter.ofPattern(NORM_DATE_PATTERN);

    /**
     * 日期格式：yyyyMMdd
     */
    public final static String            PURE_DATE_PATTERN = "yyyyMMdd";
    /**
     * 日期格式：yyyyMMdd
     */
    public final static DateTimeFormatter PURE_DATE_FORMAT  = DateTimeFormatter.ofPattern(PURE_DATE_PATTERN);

    /**
     * 标准日期时间格式，精确到秒：yyyy-MM-dd HH:mm:ss
     */
    public final static String            NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 标准日期时间格式，精确到秒：yyyy-MM-dd HH:mm:ss
     */
    public final static DateTimeFormatter NORM_DATETIME_FORMAT  = DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN);

    /**
     * 标准日期时间格式，精确到秒：yyyy-MM-dd HH:mm:ss.SSS
     */
    public final static String            NORM_DATETIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * 标准日期时间格式，精确到秒：yyyy-MM-dd HH:mm:ss.SSS
     */
    public final static DateTimeFormatter NORM_DATETIME_MS_FORMAT  = DateTimeFormatter.ofPattern(NORM_DATETIME_MS_PATTERN);

    public static String formatTimeStamp(long timeStamp, DateTimeFormatter formatter) {
        LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), ZoneOffset.systemDefault());
        return time.format(formatter);
    }

    public static Date parseDate(String text, DateTimeFormatter formatter) {
        return Date.from(LocalDateTime.parse(text, formatter).atZone(ZoneOffset.systemDefault()).toInstant());
    }

    public static LocalDate parseLocalDate(String text, DateTimeFormatter formatter) {
        return LocalDate.parse(text, formatter);
    }

    public static Date parseDateAtMin(String text, DateTimeFormatter formatter) {
        LocalDateTime time = LocalDate.parse(text, formatter).atTime(0, 0);
        return Date.from(time.atZone(ZoneOffset.systemDefault()).toInstant());
    }

    public static Date parseDateAtMax(String text, DateTimeFormatter formatter) {
        LocalDateTime time = LocalDate.parse(text, formatter).atTime(23, 59, 59, 999999999);
        return Date.from(time.atZone(ZoneOffset.systemDefault()).toInstant());
    }

    public static boolean checkDate4yyyyMMdd(int... dates) {
        if (dates == null || dates.length == 0) {
            return true;
        }
        for (int date : dates) {
            if (date / 10000000 <= 0 || date / 100000000 > 0) {
                return false;
            }
            try {

            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return true;
    }

    /**
     * 对时间格式为yyyyMMdd的日期进行加减天数的操作
     *
     * @return
     */
    public static int incrDate(int date, int diff) {
        if (diff == 0) {
            return date;
        }
        LocalDate dateTime = LocalDate.parse(date + "", PURE_DATE_FORMAT);
        if (diff > 0) {
            dateTime = dateTime.plusDays(diff);
        } else if (diff < 0) {
            dateTime = dateTime.minusDays(-diff);
        }
        return Integer.valueOf(dateTime.format(PURE_DATE_FORMAT)).intValue();
    }

    /**
     * 验证返回时间格式为yyyyMMdd的日期
     *
     * @return
     */
    public static LocalDate parseLocalDate(int date) {
        if (date / 10000000 <= 0 || date / 100000000 > 0) {
            return null;
        }
        try {
            return LocalDate.parse(date + "", PURE_DATE_FORMAT);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 返回时间范围内的时间
     *
     * @param from
     * @param to
     * @return
     */
    public static LocalDate[] betweenLocalDates(LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isAfter(to)) {
            return null;
        }
        if (from.equals(to)) {
            return new LocalDate[]{from};
        }
        int num = (int) ChronoUnit.DAYS.between(from, to) + 1;
        LocalDate[] dates = new LocalDate[num];
        for (int i = 0; i < num; i++) {
            dates[i] = from.plusDays(i);
        }
        return dates;
    }

    /**
     * 返回时间范围内的时间，格式yyyyMMdd
     *
     * @param from
     * @param to
     * @return
     */
    public static int[] betweenDaysToPureDate(LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isAfter(to)) {
            return null;
        }
        if (from.equals(to)) {
            return new int[]{Integer.valueOf(from.format(PURE_DATE_FORMAT)).intValue()};
        }
        int num = (int) ChronoUnit.DAYS.between(from, to) + 1;
        int[] dates = new int[num];
        for (int i = 0; i < num; i++) {
            dates[i] = Integer.valueOf(from.plusDays(i).format(PURE_DATE_FORMAT)).intValue();
        }
        return dates;
    }

    /**
     * 返回时间范围内的时间，格式yyyy-MM-dd
     *
     * @param from
     * @param to
     * @return
     */
    public static String[] betweenDaysToNormDate(LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isAfter(to)) {
            return null;
        }
        if (from.equals(to)) {
            return new String[]{from.format(NORM_DATE_FORMAT)};
        }
        int num = (int) ChronoUnit.DAYS.between(from, to) + 1;
        String[] dates = new String[num];
        for (int i = 0; i < num; i++) {
            dates[i] = from.plusDays(i).format(NORM_DATE_FORMAT);
        }
        return dates;
    }

    /**
     * 返回时间范围内的天数
     *
     * @param from
     * @param to
     * @return
     */
    public static int betweenDaysNum(LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isAfter(to)) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(from, to) + 1;
    }
}
