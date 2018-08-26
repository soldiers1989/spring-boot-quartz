package com.pgy.ginko.quartz.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ginko
 */
public enum YesNoStatus {

    YES(1, "是"), NO(0, "否");

    private Integer code;

    private String name;


    private static Map<Integer, YesNoStatus> codeMap = null;

    YesNoStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static YesNoStatus findEnumByCode(Integer code) {
        for (YesNoStatus roleType : YesNoStatus.values()) {
            if (roleType.getCode().equals(code)) {
                return roleType;
            }
        }
        return null;
    }

    public static Map<Integer, YesNoStatus> getCodeEnumsMap() {
        if (codeMap != null && codeMap.size() > 0) {
            return codeMap;
        }
        codeMap = new HashMap<>();
        for (YesNoStatus item : YesNoStatus.values()) {
            codeMap.put(item.getCode(), item);
        }
        return codeMap;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
