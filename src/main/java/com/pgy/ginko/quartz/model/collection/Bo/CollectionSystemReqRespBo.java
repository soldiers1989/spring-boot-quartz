package com.pgy.ginko.quartz.model.collection.Bo;

import lombok.Data;
import lombok.ToString;

/**
 * 催收平台请求响应返呗结果
 * @author ginko
 * @date 2018-8-26 11:03:37
 */
@Data
@ToString
public class CollectionSystemReqRespBo {

    private String code;

    private String msg;

    private String data;

    private String sign;

    private String timestamp;

}
