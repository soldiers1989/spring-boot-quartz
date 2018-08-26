package com.pgy.ginko.quartz.model.collection.Bo;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * 催收平台请求bo
 *
 * @author ginko
 * @date 2018-8-26 11:03:37
 */
@Getter
@ToString
public class CollectionSystemDataBo extends HashMap<String, Object> {

    private static final long serialVersionUID = -6891063734366973441L;

    private String sign;//MD5签名,对data的json串签名

    private String timestamp;

    private String dataType;//数据类型

    private String data;//数据集合

    public void setSign(String sign) {
        this.sign = sign;
        this.put("sign", sign);
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        this.put("timestamp", timestamp);
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
        this.put("dataType", dataType);
    }

    public void setData(String data) {
        this.data = data;
        this.put("data", data);
    }

}
