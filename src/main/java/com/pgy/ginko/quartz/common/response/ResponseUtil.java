package com.pgy.ginko.quartz.common.response;

import com.pgy.ginko.quartz.common.constant.CommonConstant;
import com.pgy.ginko.quartz.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Generate response for request
 *
 * @author ginko
 * @date 2018-8-23 18:37:26
 */
@Slf4j
public class ResponseUtil {

    /**
     * return response with default success or error message by status
     *
     * @param resultStatus
     * @return
     */
    public static CommonResponse generateResponse(boolean resultStatus) {
        CommonResponse commonResponse = new CommonResponse();
        if (resultStatus) {
            commonResponse
                    .setCode(ResponseCode.SUCCESS)
                    .setMessage(CommonConstant.DEFAULT_SUCCESS_MESSAGE);
        } else {
            commonResponse
                    .setCode(ResponseCode.FAIL)
                    .setMessage(CommonConstant.DEFAULT_FAIL_MESSAGE);
        }
        return commonResponse;
    }

    /**
     * return response with custom message by status
     *
     * @param message
     * @param resultStatus
     * @return
     */
    public static CommonResponse generateResponse(String message, boolean resultStatus) {
        CommonResponse commonResponse = new CommonResponse();
        if (resultStatus) {
            commonResponse
                    .setCode(ResponseCode.SUCCESS)
                    .setMessage(message);
        } else {
            commonResponse
                    .setCode(ResponseCode.FAIL)
                    .setMessage(message);
        }
        return commonResponse;
    }

    /**
     * return response with data,if data is null,return no data message,or return data
     *
     * @param data
     * @return
     */
    public static CommonResponse generateResponse(Object data) {
        CommonResponse commonResponse = new CommonResponse();
        if (data != null) {
            commonResponse
                    .setCode(ResponseCode.SUCCESS)
                    .setMessage(CommonConstant.DEFAULT_SUCCESS_MESSAGE)
                    .setData(data);
        } else {
            commonResponse
                    .setCode(ResponseCode.SUCCESS)
                    .setMessage(CommonConstant.NO_RESULT_MESSAGE);

        }
        return commonResponse;
    }

    /**
     * Handler response information
     *
     * @param response
     * @param object
     * @return
     */
    public static HttpServletResponse handlerResponse(HttpServletResponse response, Object object) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);
        try {
            response.getWriter().write(JSONUtil.toJSONString(object));
        } catch (IOException e) {
            log.error("response: ",e);
        }
        return response;
    }
}
