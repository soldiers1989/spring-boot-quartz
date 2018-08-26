package com.pgy.ginko.quartz.service.biz;

import com.pgy.ginko.quartz.model.biz.SmsChannelDo;
import org.springframework.stereotype.Component;

/**
 * 短信通道服务
 *
 * @author ginko
 * @date 2018/4/17
 */
@Component(value = "smsChannelService")
public interface SmsChannelService {

    SmsChannelDo findSmsChannel(SmsChannelDo query);

}
