package com.pgy.ginko.quartz.common.enums;

import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 向催收平台传输逾期类型
 *
 * @author ginko
 * @date 2018/8/17
 */

public enum TransOverdueBorrowCashType {
	
	ADD("add", "新增方式"), 
	UPDATE("update", "更新方式");


    private String code;

    private String name;

    private static Map<String, TransOverdueBorrowCashType> transOverdueBorrowCashTypeMap = null;

    TransOverdueBorrowCashType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static TransOverdueBorrowCashType findByCode(String code) {
        for (TransOverdueBorrowCashType transOverdueBorrowCashType : TransOverdueBorrowCashType.values()) {
            if (transOverdueBorrowCashType.getCode().equals(code)) {
                return transOverdueBorrowCashType;
            }
        }
        return null;
    }

    public static Map<String, TransOverdueBorrowCashType> getTransOverdueBorrowCashTypeMap() {
        if (!CollectionUtils.isEmpty(transOverdueBorrowCashTypeMap)) {
            return transOverdueBorrowCashTypeMap;
        }
        transOverdueBorrowCashTypeMap = new HashMap<>();
        for (TransOverdueBorrowCashType item : TransOverdueBorrowCashType.values()) {
            transOverdueBorrowCashTypeMap.put(item.getCode(), item);
        }
        return transOverdueBorrowCashTypeMap;
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
