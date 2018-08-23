package com.pgy.ginko.quartz.common;

import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ginko
 * @description 配置数据源key
 * @date 2018/8/23 10:34
 */
public enum DataSourceKey {
    /**
     * product_master 数据库 product_slaveAlpha 数据库 product_slaveBeta 数据库product_slaveGamma 数据库
     */
    BIZ(0, "biz"),

    COLLECTION(1, "collection"),

    RISK(2, "risk"),

    UPS(3, "ups"),

    QUARTZ(4, "quartz");

    private Integer code;

    private String name;

    private static Map<Integer, DataSourceKey> dataSourceKeyMap = null;

    DataSourceKey(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String findNameByCode(Integer code) {
        for (DataSourceKey dataSourceKey : DataSourceKey.values()) {
            if (dataSourceKey.getCode().intValue() == code.intValue()) {
                return dataSourceKey.getName();
            }
        }

        return null;
    }

    public static Integer findCodeByName(String name) {
        for (DataSourceKey dataSourceKey : DataSourceKey.values()) {
            if (dataSourceKey.getName().equals(name)) {
                return dataSourceKey.getCode();
            }
        }
        return null;
    }

    public static DataSourceKey findDataSourceKeyByName(String name) {
        for (DataSourceKey dataSourceKey : DataSourceKey.values()) {
            if (dataSourceKey.getName().equals(name)) {
                return dataSourceKey;
            }
        }
        return null;
    }

    public static Map<Integer, DataSourceKey> findDataSourceKeyMap() {
        if (!CollectionUtils.isEmpty(dataSourceKeyMap)) {
            return dataSourceKeyMap;
        }
        dataSourceKeyMap = new HashMap<>();
        for (DataSourceKey item : DataSourceKey.values()) {
            dataSourceKeyMap.put(item.getCode(), item);
        }
        return dataSourceKeyMap;
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