package com.pgy.ginko.quartz.common.enums;

import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ginko
 */
public enum ResourceType {

    OVERDUE_CONFIG("OVERDUE_CONFIG", "逾期费比例"),

    COLLECTION_DATE_CONFIGURATION("COLLECTION_DATE_CONFIGURATION", "同步至催收新增数据逾期日期设置"),

    SYNC_COLLECTION_FAILED_SEND_OPERATOR("SYNC_COLLECTION_FAILED_SEND_OPERATOR", "催收同步异常,发送短信给指定接收人!");

    private String code;

    private String name;

    private static Map<String, ResourceType> resourceTypeMap = null;

    ResourceType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ResourceType findByCode(String code) {
        for (ResourceType goodSource : ResourceType.values()) {
            if (goodSource.getCode().equals(code)) {
                return goodSource;
            }
        }
        return null;
    }

    public static Map<String, ResourceType> getCodeRoleTypeMap() {
        if (!CollectionUtils.isEmpty(resourceTypeMap)) {
            return resourceTypeMap;
        }
        resourceTypeMap = new HashMap<>();
        for (ResourceType item : ResourceType.values()) {
            resourceTypeMap.put(item.getCode(), item);
        }
        return resourceTypeMap;
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
