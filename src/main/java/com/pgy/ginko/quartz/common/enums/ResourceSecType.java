package com.pgy.ginko.quartz.common.enums;

import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ginko
 */
public enum ResourceSecType {

    OVERDUE_CONFIG("OVERDUE_CONFIG", "逾期费比例"),

    COLLECTION_DATE_CONFIGURATION("COLLECTION_DATE_CONFIGURATION", "同步至催收新增数据逾期日期设置");

    private String code;

    private String name;

    private static Map<String, ResourceSecType> resourceSecType = null;

    ResourceSecType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ResourceSecType findByCode(String code) {
        for (ResourceSecType resourceSecType : ResourceSecType.values()) {
            if (resourceSecType.getCode().equals(code)) {
                return resourceSecType;
            }
        }
        return null;
    }

    public static Map<String, ResourceSecType> getResourceSecTypeMap() {
        if (!CollectionUtils.isEmpty(resourceSecType)) {
            return resourceSecType;
        }
        resourceSecType = new HashMap<>();
        for (ResourceSecType item : ResourceSecType.values()) {
            resourceSecType.put(item.getCode(), item);
        }
        return resourceSecType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
