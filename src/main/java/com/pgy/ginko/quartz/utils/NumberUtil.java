package com.pgy.ginko.quartz.utils;

import org.apache.commons.lang3.StringUtils;
import java.math.BigDecimal;


/**
 * 
 * 类NumberUtil.java的实现描述： 数字相关的工具类
 * @author 陈金虎 2015年12月20日 下午2:56:09
 */
public class NumberUtil {

    /**
     * <p>
     * String convert to int,if string is null or empty or string convert failed, return -1;else return
     * Integer.parseInt(string);
     * </p>
     * 
     * @code{Integer for example:
     * 
     * <pre>
     * strToInt(null) = -1;
     * strToInt('') = -1;
     * strToInt('  ') = -1;
     * strToInt('sb') = -1;
     * strToInt('100') = 100;
     * </pre>
     * @param str
     */
    public static int strToInt(String str) {
        return strToIntWithDefault(str, -1);
    }

    /**
     * <p>
     * String convert to int with defaultInt ,if string is null or empty or string convert failed, return default;else
     * return Integer.parseInt(string);
     * </p>
     * 
     * @code{Integer for example:
     * 
     * <pre>
     * strToInt(null) = defaultInt;
     * strToInt('') = defaultInt;
     * strToInt('  ') = defaultInt;
     * strToInt('sb') = defaultInt;
     * strToInt('100') = 100;
     * </pre>
     * @param str
     */
    public static int strToIntWithDefault(String str, int def) {
        if (StringUtils.isBlank(str)) {
            return def;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return def;
        }
    }
    
    public static Integer strToIntWithDefault(String str, Integer def) {
        if (StringUtils.isBlank(str)) {
            return def;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * <p>
     * String convert to long,if obj is null or convert failed, return -1l;else return Long.parseLong(obj.toString());
     * </p>
     * <code> Long </code> for example:
     *
     * <pre>
     * objToLong(null) = -1l;
     * objToLong('sb') = -1l;
     * objToLong('100l') = 100l;
     * </pre>
     * 
     * @param obj
     */
    public static long objToLong(Object obj) {
        return objToLongWithDefault(obj, -1l);
    }

    /**
     * <p>
     * String convert to long,if obj is null or convert failed, return defLong;else return Long.parseLong(string);
     * </p>
     * <code> Long </code> for example:
     * 
     * <pre>
     * objToLongWithDefault(null, defLong) = defLong;
     * objToLongWithDefault('sb',defLong) = defLong;
     * objToLongWithDefault('100l',defLong) = 100l;
     * </pre>
     * 
     * @param obj
     */
    public static long objToLongWithDefault(Object obj, long def) {
        if (null == obj) {
            return def;
        }
        try {
            return Long.parseLong(obj.toString());
        } catch (Exception e) {
            return def;
        }
    }
    
    public static Long objToLongWithDefault(Object obj, Long def) {
        if (null == obj) {
            return def;
        }
        try {
            return Long.parseLong(obj.toString());
        } catch (Exception e) {
            return def;
        }
    }


    /**
     * <p>
     * String convert to int,if obj is null or convert failed, return -1;else return Integer.parseInt(obj.toString());
     * </p>
     * <code> Integer </code> for example:
     *
     * <pre>
     * objToInt(null) = -1;
     * objToInt('sb') = -1;
     * objToInt('100') = 100;
     * </pre>
     * 
     * @param obj
     */
    public static int objToInt(Object obj) {
        return objToIntWithDefault(obj, -1);
    }

    /**
     * <p>
     * String convert to int,if obj is null or convert failed, return defInt;else return Integer.parseInt(string);
     * </p>
     * 
     * <code> Integer </code>  for example:
     * 
     * <pre>
     * objToInt(null) = defInt;
     * objToInt('sb') = defInt;
     * objToInt('100') = 100;
     * </pre>
     * @param obj
     */
    public static int objToIntWithDefault(Object obj, int def) {
        if (null == obj) {
            return def;
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            return def;
        }
    }

    public static long strToLong(String str) {
        return strToLongWithDefault(str, -1l);
    }

    public static long strToLongWithDefault(String str, long def) {
        if (StringUtils.isBlank(str)) {
            return def;
        }
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return def;
        }
    }

    public static double strToDouble(String str) {
        return strToDoubleWithDefault(str, -1d);
    }

    public static double strToDoubleWithDefault(String str, double def) {
        if (StringUtils.isBlank(str)) {
            return def;
        }
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return def;
        }
    }
    
    public static BigDecimal objToBigDecimalDefault(Object obj, BigDecimal defaultValue) {
        if (null == obj) {
            return defaultValue;
        }
        try {
            return new BigDecimal(obj.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static Long objToLongDefault(Object obj, long defaultValue) {
        if (null == obj) {
            return defaultValue;
        }
        try {
            return Long.parseLong(obj.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static Long objToLongDefault(Object obj, Long defaultValue) {
        if (null == obj) return defaultValue;
        try {
            return Long.parseLong(obj.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static Boolean objToBooleanDefault(Object obj, Boolean defaultValue) {
        if (null == obj) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(obj.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static Long objToPageLongDefault(Object obj, Long defaultValue) {
        if (null == obj) {
            return defaultValue;
        }
        try {
        	Long pageNum = Long.parseLong(obj.toString()); 
            return pageNum == 0L ? 1L : pageNum;
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static int objToIntDefault(Object obj, int defaultValue) {
        if (null == obj) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static Integer objToIntDefault(Object obj, Integer defaultValue) {
        if (null == obj) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 数字字符串大小比较
     * n1>n2 返回1 n1=n2 返回0 n1<n2 返回2
     * -1失败
     * @param n1
     * @param n2
     * @return
     */
    public static Integer compareN1ToN2(String n1,String n2){
        if(StringUtils.isNumeric(n1) && StringUtils.isNumeric(n2)){
            int i1 = Integer.parseInt(n1);
            int i2 = Integer.parseInt(n2);
            if(i1>i2){
               return 1;
            }
            else if(i1 == i2){
                return 0;
            }
            else{
                return 2;
            }
        }else{
            return -1;
        }
    }
}
