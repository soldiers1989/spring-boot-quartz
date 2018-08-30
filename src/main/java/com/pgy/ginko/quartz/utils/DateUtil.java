

package com.pgy.ginko.quartz.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author ginko
 * @date 2018-8-25 23:56:48
 */
@Slf4j
public class DateUtil {

    /**
     * 格式 ：yyyy-MM-dd HH:mm:ss
     */
    public static final String DATEFORMAT_STR_001 = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式 ：yyyyMMddHHmmssSSS
     */

    public static final String DATE_TIME_FULL_ALL = "yyyyMMddHHmmssSSS";

    /**
     * 格式 ：yyyy-MM-dd
     */
    public static final String DATEFORMAT_STR_002 = "yyyy-MM-dd";
    /**
     * 格式 ：MM-dd
     */
    public static final String DATEFORMAT_STR_003 = "MM-dd";
    /**
     * 格式 ：HH:mm:ss
     */
    public static final String DATEFORMAT_STR_004 = "HH:mm:ss";
    /**
     * 格式 ：yyyy-MM
     */
    public static final String DATEFORMAT_STR_005 = "yyyy-MM";

    /**
     * 格式 ：yyyyMMddHHmmss
     */
    public static final String DATEFORMAT_STR_006 = "yyyyMMddHHmmss";
    /**
     * 格式 ：yyyyMMdd
     */
    public static final String DATEFORMAT_STR_007 = "yyyyMMdd";

    /**
     * 格式 ：yyyy年MM月dd日 HH时mm分ss秒
     */
    public static final String DATEFORMAT_STR_008 = "yyyy年MM月dd日 HH时mm分ss秒";
    /**
     * 格式 ：yyyy年MM月dd日
     */
    public static final String DATEFORMAT_STR_009 = "yyyy年MM月dd日";
    /**
     * 格式 ：MM月dd日 hh:mm
     */
    public static final String DATEFORMAT_STR_010 = "MM月dd日 hh:mm";


    /**
     * 获得当前日期
     *
     * @return
     */
    public static Date getNow() {
        Calendar cal = Calendar.getInstance();
        Date currDate = cal.getTime();
        return currDate;
    }

    /**
     * 日期转换为字符串 格式自定义
     *
     * @param date
     * @param f
     * @return
     */
    public static String dateStr(Date date, String f) {
        SimpleDateFormat format = new SimpleDateFormat(f);
        if (date != null) {
            return format.format(date);
        }
        return "";
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String dateStr001(Date date) {
        return dateStr(date, DATEFORMAT_STR_001);

    }

    /**
     * yyyyMMdd
     *
     * @param date
     * @return
     */
    public static String dateStr007(Date date) {
        return dateStr(date, DATEFORMAT_STR_007);
    }

    /**
     * Add specified number of days to the given date.
     *
     * @param date date
     * @param days Int number of days to add
     * @return revised date
     */
    public static Date addDays(final Date date, int days) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);

        return new Date(cal.getTime().getTime());
    }

    /**
     * Get start of date.
     *
     * @param date Date
     * @return Date Date
     */
    public static Date getStartOfDate(final Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return new Date(cal.getTime().getTime());
    }

    /**
     * 一天的结束时间，【注：只精确到毫秒】
     *
     * @param date
     * @return
     */
    public static Date getEndOfDate(final Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);

        return new Date(cal.getTime().getTime());
    }

    /**
     * 比较两个时间大小
     *
     * @param date1
     * @param date2 date1>date2= true
     * @return
     */
    public static boolean compare(Date date1, Date date2) {
        DateFormat sdf = new SimpleDateFormat(DATEFORMAT_STR_001);
        Calendar cal = Calendar.getInstance();
        boolean b = false;
        try {
            Date d1 = sdf.parse(DateUtil.dateStr001(date1));
            Date d2 = sdf.parse(DateUtil.dateStr001(date2));
            cal.setTime(d1);
            long time1 = cal.getTimeInMillis();
            cal.setTime(d2);
            long time2 = cal.getTimeInMillis();
            if (time1 > time2) {
                b = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param date1 开始时间
     * @param date2 结束时间
     * @return
     */
    public static int daysBetween(Date date1, Date date2) {
        DateFormat sdf = new SimpleDateFormat(DATEFORMAT_STR_007);
        Calendar cal = Calendar.getInstance();
        try {
            Date d1 = sdf.parse(DateUtil.dateStr007(date1));
            Date d2 = sdf.parse(DateUtil.dateStr007(date2));
            cal.setTime(d1);
            long time1 = cal.getTimeInMillis();
            cal.setTime(d2);
            long time2 = cal.getTimeInMillis();
            return Integer.parseInt(String.valueOf((time2 - time1) / 86400000L));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 计算两个日期之间相差的小时数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int hoursBetween(Date date1, Date date2) {

        DateFormat sdf = new SimpleDateFormat(DATEFORMAT_STR_007);
        Calendar cal = Calendar.getInstance();
        try {
            Date d1 = sdf.parse(DateUtil.dateStr007(date1));
            Date d2 = sdf.parse(DateUtil.dateStr007(date2));
            cal.setTime(d1);
            long time1 = cal.getTimeInMillis();
            cal.setTime(d2);
            long time2 = cal.getTimeInMillis();
            return Integer.parseInt(String.valueOf((time2 - time1) / 3600000L));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 计算两个日期之间相差的分钟数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int minuteBetween(Date date1, Date date2) {

        DateFormat sdf = new SimpleDateFormat(DATEFORMAT_STR_001);
        Calendar cal = Calendar.getInstance();
        try {
            Date d1 = sdf.parse(DateUtil.dateStr001(date1));
            Date d2 = sdf.parse(DateUtil.dateStr001(date2));
            cal.setTime(d1);
            long time1 = cal.getTimeInMillis();
            cal.setTime(d2);
            long time2 = cal.getTimeInMillis();
            return Integer.parseInt(String.valueOf((time2 - time1) / (1000 * 60)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String formatDate(final Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String formatWithDateTimeShort(final Date date) {
        if (date == null) {
            return "";
        }
        return formatDate(date, DATEFORMAT_STR_001);
    }

    public static String formatWithDateTimeFull(final Date date) {
        if (date == null) {
            return "";
        }
        return formatDate(date, DATEFORMAT_STR_001);
    }

    public static String formatWithDateTimeFullAll(final Date date) {
        if (date == null) {
            return "";
        }
        return formatDate(date, DATE_TIME_FULL_ALL);
    }

}
