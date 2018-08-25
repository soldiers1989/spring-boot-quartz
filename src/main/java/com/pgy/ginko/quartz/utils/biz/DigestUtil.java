package com.pgy.ginko.quartz.utils.biz;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.util.Strings;

/**
 * @author ginko
 */
@Slf4j
public class DigestUtil {

    public static String MD5(String md5Str) {
        if (Strings.isBlank(md5Str)) {
            return null;
        }
        return DigestUtils.md5Hex(md5Str);
    }

}
