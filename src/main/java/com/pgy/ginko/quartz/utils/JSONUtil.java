package com.pgy.ginko.quartz.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;


/**
 * @author ginko
 * @description JSON data util, for parse and generate JSON data
 * @date 2017-07-11 16:11
 */
@Slf4j
public class JSONUtil {

    /**
     * Transfer object to JSON string
     *
     * @param object
     * @return
     */
    public static String toJSONString(Object object) {
        String result = null;
        ObjectMapper objectMapper = new ObjectMapper();
        //set config of JSON
        // can use single quote
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //allow unquoted field names
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        //set date format
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        try {
            result = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Generate JSON String error!" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
