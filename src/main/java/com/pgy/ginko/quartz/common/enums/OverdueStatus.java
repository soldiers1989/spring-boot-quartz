package com.pgy.ginko.quartz.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangmx
 */
public enum OverdueStatus {
    NO_OVERDUE(0, "未逾期"),
    OVERDUE(1, "已逾期");

    private Integer code;
    private String name;

    private static Map<Integer, OverdueStatus> overdueStatusMap = null;

    OverdueStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String findNameByCode(Integer code) {
        for (OverdueStatus roleType : OverdueStatus.values()) {
            if (roleType.getCode().intValue() == code.intValue()) {
                return roleType.getName();
            }
        }
        return null;
    }

    public static Integer findCodeByName(String name) {
        for (OverdueStatus roleType : OverdueStatus.values()) {
            if (roleType.getName().equals(name)) {
                return roleType.getCode();
            }
        }
        return null;
    }

    public static Map<Integer, OverdueStatus> getOverdueStatusMap() {
        if (overdueStatusMap != null && overdueStatusMap.size() > 0) {
            return overdueStatusMap;
        }
        overdueStatusMap = new HashMap<>();
        for (OverdueStatus item : OverdueStatus.values()) {
            overdueStatusMap.put(item.getCode(), item);
        }
        return overdueStatusMap;
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
