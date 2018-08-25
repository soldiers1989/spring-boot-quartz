package com.pgy.ginko.quartz.common.constant;

import java.time.format.DateTimeFormatter;

/**
 * Common constant
 *
 * @author ginko
 * @date 2018-8-23 18:37:26
 */
public class CommonConstant {

    /**
     * Common data type
     */
    public static final String STRING_ZERO = "0";
    public static final String STRING_ONE= "1";

    public static final Long LONG_ZERO= 0L;
    public static final Long LONG_ONE= 1L;

    /**
     * HTTP Status Code
     *
     *  200： 服务器成功返回网页
     *  301： 永久性重定向
     *  302： 临时性重定向
     *  403： 没有权限访问
     *  404： 表示没有对应的资源
     *  500： 服务器错误
     *  503： 服务器停机或正在维护
     *
     */
    public static final Integer HTTP_STATUS_200 = 200;
    public static final Integer HTTP_STATUS_301 = 301;
    public static final Integer HTTP_STATUS_302 = 302;
    public static final Integer HTTP_STATUS_403 = 403;
    public static final Integer HTTP_STATUS_404  = 404;
    public static final Integer HTTP_STATUS_500  = 500;
    public static final Integer HTTP_STATUS_503  =503 ;

    /**
     * Request result code
     */
    public static final Integer DEFAULT_SUCCESS_CODE = 10000;
    public static final Integer DEFAULT_FAILED_CODE = 9999;
    public static final String DEFAULT_SUCCESS_CODE_ZERO4 = "0000";

    /**
     * Request result message
     */
    public static final String DEFAULT_SUCCESS_MESSAGE = "success";
    public static final String DEFAULT_FAIL_MESSAGE = "fail";
    public static final String NO_RESULT_MESSAGE = "no result";

    /**
     * Operation status
     */
    public static final String RESULT = "result";
    public static final String SUCCESS = "SUCCESS";
    public static final String ERROR = "ERROR";

    /**
     * Error or exception message
     */
    public static final String DB_ERROR_MESSAGE = "Database Error";
    public static final String SERVER_ERROR_MESSAGE = "Server Error";

    /**
     * Time
     */
    public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    public static final long SECOND_OF_ONE_DAY = 24 * 60 * 60L;
    public static final long SECOND_OF_ONE_WEEK = 90 * 24 * 60 * 60L;
    public static final long SECOND_OF_TWO_DAY = 2 * 24 * 60 * 60L;
    public static final long SECOND_OF_THREE = 30L;


    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

}
