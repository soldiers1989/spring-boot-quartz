package com.pgy.ginko.quartz.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 金额相关计算
 */
public class BigDecimalUtil {

    public static final BigDecimal ONE_HUNDRED = new BigDecimal("100.00");

    /**
     * 加法,保留小数点两位
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
        v1 = v1 == null ? new BigDecimal(0) : v1;
        v2 = v2 == null ? new BigDecimal(0) : v2;
        v1 = v1.add(v2).setScale(2, RoundingMode.HALF_UP);
        return v1;
    }

    /**
     * 加法,保留小数点两位
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal add(double v1, double v2) {
        return new BigDecimal(v1).add(new BigDecimal(v2)).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 减法v1-v2
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal subtract(BigDecimal v1, BigDecimal v2) {
        v1 = v1 == null ? new BigDecimal(0) : v1;
        v2 = v2 == null ? new BigDecimal(0) : v2;
        v1 = v1.subtract(v2).setScale(2, RoundingMode.HALF_UP);
        return v1;
    }

    /**
     * 减法v1-v2
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal subtract(double v1, double v2) {
        BigDecimal value = new BigDecimal(v1).subtract(new BigDecimal(v2)).setScale(2, RoundingMode.HALF_UP);
        return value;
    }

    /**
     * 乘法 v1*v2
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal multiply(double v1, double v2) {
        BigDecimal value = new BigDecimal(v1).multiply(new BigDecimal(v2)).setScale(2, RoundingMode.HALF_UP);
        return value;
    }

    /**
     * 乘法 v1*v2
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal multiply(BigDecimal v1, BigDecimal v2) {
        v1 = v1 == null ? new BigDecimal(0) : v1;
        v2 = v2 == null ? new BigDecimal(0) : v2;
        v1 = v1.multiply(v2).setScale(2, RoundingMode.HALF_UP);
        return v1;
    }

    /**
     * 除法 v1/v2(v2为0时未抛异常，注意不传空)
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal divide(double v1, double v2) {
        BigDecimal value = new BigDecimal(v1).divide(new BigDecimal(v2), 2, RoundingMode.HALF_UP);
        return value;
    }

    /**
     * 除法 v1/v2(v2为0时未抛异常，注意不传空)
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal divide(BigDecimal v1, BigDecimal v2) {
        v1 = v1 == null ? new BigDecimal(0) : v1;
        v2 = v2 == null ? new BigDecimal(0) : v2;
        v1 = v1.divide(v2, 2, RoundingMode.HALF_UP);
        return v1;
    }

    /**
     * 多个相乘
     *
     * @param array
     * @return
     */
    public static BigDecimal multiply(BigDecimal... array) {
        BigDecimal result = BigDecimal.ONE;
        if (array == null || array.length == 0) {
            return BigDecimal.ZERO;
        }
        for (int i = 0; i < array.length; i++) {
            result = multiply(result, array[i]);
        }
        return result;
    }

    /**
     * 多个想加
     *
     * @return
     */
    public static BigDecimal add(BigDecimal... array) {
        BigDecimal result = BigDecimal.ZERO;
        if (array == null || array.length == 0) {
            return BigDecimal.ZERO;
        }
        for (int i = 0; i < array.length; i++) {
            result = add(result, array[i]);
        }
        return result;
    }

    public static Long removeNull(Long v1) {
        if (v1 == null) {
            return 0l;
        }
        return v1;
    }


}