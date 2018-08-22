package com.pgy.ginko.quartz.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import static com.pgy.ginko.quartz.common.CommonConstant.DATE_TIME;
import static com.pgy.ginko.quartz.common.CommonConstant.DATE_TIME_FORMATTER;

/**
 * @description The type Application utils.
 * @author ginko
 * @date 2018-8-22 13:19:26
 */
public class ApplicationUtils {

    /**
     * Format date string.
     *
     * @param date the date
     * @return the string
     */
    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME);
        return simpleDateFormat.format(date);
    }

    /**
     * Get current datetime
     *
     * @return string
     */
    public static String currentDateTime() {
        return LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }
}
