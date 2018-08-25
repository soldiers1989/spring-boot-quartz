package com.pgy.ginko.quartz.common.response;


import lombok.ToString;

/**
 * Response bean for format response
 *
 * @author ginko
 * @date 2018-8-23 18:37:26
 */
@ToString
public class CommonResponse {

    private int code;

    private String message;

    private Object data;

    public static CommonResponse getNewInstance() {
        return new CommonResponse();
    }

    public int getCode() {
        return code;
    }

    public CommonResponse setCode(ResponseCode responseCode) {
        this.code = responseCode.code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CommonResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public CommonResponse setData(Object data) {
        this.data = data;
        return this;
    }

}
