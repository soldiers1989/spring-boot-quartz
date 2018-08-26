package com.pgy.ginko.quartz.service.biz.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pgy.ginko.quartz.common.constant.BizConstants;
import com.pgy.ginko.quartz.common.constant.CommonConstant;
import com.pgy.ginko.quartz.common.enums.SmsChannelCodeEnum;
import com.pgy.ginko.quartz.common.enums.SmsUsefulEnum;
import com.pgy.ginko.quartz.common.http.HttpResult;
import com.pgy.ginko.quartz.common.sms.SmsSendRequest;
import com.pgy.ginko.quartz.common.sms.SmsSendResponse;
import com.pgy.ginko.quartz.model.biz.SmsChannelDo;
import com.pgy.ginko.quartz.service.biz.SmsChannelService;
import com.pgy.ginko.quartz.service.biz.impl.HttpApiService;
import com.pgy.ginko.quartz.utils.biz.ChuangLanSmsUtil;
import com.pgy.ginko.quartz.utils.biz.DigestUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * @author ginko
 */
@Slf4j
@Component("smsUtil")
public class SmsUtil {

    private final static String SIGN = "美期分期";

    @Value("${spring.profiles.active}")
    private static String env;//当前激活的配置文件

    @Resource
    private SmsChannelService smsChannelService;

    @Resource
    private HttpApiService httpApiService;

    public boolean sendZhPushException(String mobile, String content) throws Exception {
        SmsChannelDo query = new SmsChannelDo();
        query.setSmsUserfulCode(SmsUsefulEnum.NORMAL.toString());
        SmsChannelDo smsChannel = smsChannelService.findSmsChannel(query);
        if (smsChannel == null) {
            return false;
        }

        SmsResult smsResult = doSendSms(mobile, content, smsChannel);
        return smsResult.isSucc();
    }

    /**
     * 发送短消息
     *
     * @param mobiles      手机号
     * @param content      短信内容
     * @param smsChannelDo 短信渠道
     */
    private SmsResult doSendSms(String mobiles, String content, SmsChannelDo smsChannelDo) throws Exception {
        SmsResult result = new SmsResult();
        if (BizConstants.ENVIRONMENT_TYPE_TEST.equals(env)) {
            result.setSucc(true);
            result.setResultStr("test");
            return result;
        }

        // 请求短信通道
        String smsChannelCode = smsChannelDo.getSmsChannelCode();
        if (smsChannelCode.contains(SmsChannelCodeEnum.DAHAN.name())) {
            Map<String, Object> paramsMap = new HashMap<>(5);
            paramsMap.put("account", smsChannelDo.getAccount());
            paramsMap.put("password", Objects.requireNonNull(DigestUtil.MD5(smsChannelDo.getPassword())).toLowerCase());
            paramsMap.put("phones", mobiles);
            paramsMap.put("content", content);
            paramsMap.put("sign", SIGN);
            HttpResult reqResult = httpApiService.doPost(smsChannelDo.getRequestUrl(), paramsMap);

            log.info("SMS channel : 【" + smsChannelDo.getSmsChannelName() + "】 sendSms params=|", mobiles, "|", content, "|", reqResult);

            JSONObject json = JSON.parseObject(reqResult.getBody());
            if (json.getInteger(CommonConstant.RESULT) == 0) {
                result.setSucc(true);
                result.setResultStr(json.getString("desc"));
            } else {
                result.setSucc(false);
                result.setResultStr(json.getString("desc"));
            }
        }

        if (smsChannelCode.contains(SmsChannelCodeEnum.CHUANGLAN.name())) {
            SmsSendRequest smsSingleRequest = new SmsSendRequest(smsChannelDo.getAccount(), smsChannelDo.getPassword(), content, mobiles, "true");
            String requestJson = JSON.toJSONString(smsSingleRequest);
            String response = ChuangLanSmsUtil.sendSmsByPost(smsChannelDo.getRequestUrl(), requestJson);
            SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);

            log.info("SMS channel : 【" + smsChannelDo.getSmsChannelName() + "】 sendSms params=|", requestJson, "|", response);

            if (smsSingleResponse != null && CommonConstant.STRING_ZERO.equals(smsSingleResponse.getCode())) {
                result.setSucc(true);
                result.setResultStr(smsSingleResponse.getErrorMsg());
            } else {
                result.setSucc(false);
                result.setResultStr(Objects.requireNonNull(smsSingleResponse).getErrorMsg());
            }
        }
        return result;
    }
}

@Data
class SmsResult {

    private boolean isSucc;

    private String resultStr;
}


