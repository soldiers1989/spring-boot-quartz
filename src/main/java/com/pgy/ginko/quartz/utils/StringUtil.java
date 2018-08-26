/*
 * Copyright (c) 2016,杭州霖梓网络科技股份有限公司 All Rights Reserved. 
 */
package com.pgy.ginko.quartz.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil extends StringUtils {


    public static String null2Str(Object str) {
        return (str != null) ? str.toString() : "";
    }

    public static Long null2Long(Object str) {
        try {
            return Long.valueOf(str.toString());
        } catch (Exception e) {
            return 0L;
        }
    }

    public static Integer null2Integer(Object str) {
        try {
            return Integer.valueOf(str.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 使用StringBuilder 来组装字符串，效率更高
     * @param strings
     * @return
     */
    public static String appendStrs(Object... strings) {
        StringBuilder sb = new StringBuilder();
        for (Object str : strings) {
            sb.append(null2Str(str));
        }
        return sb.toString();
    }

}
