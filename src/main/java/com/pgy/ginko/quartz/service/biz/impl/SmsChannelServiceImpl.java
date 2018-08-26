package com.pgy.ginko.quartz.service.biz.impl;

import com.pgy.ginko.quartz.annotation.DataSource;
import com.pgy.ginko.quartz.common.enums.DataSourceKey;
import com.pgy.ginko.quartz.dao.biz.SmsChannelDao;
import com.pgy.ginko.quartz.model.biz.SmsChannelDo;
import com.pgy.ginko.quartz.service.biz.SmsChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.BaseMapper;

import javax.annotation.Resource;

/**
 * Created by Wangmx on 2018/8/26
 */
@Slf4j
@DataSource(DataSourceKey.BIZ)
@Service("smsChannelServiceImpl")
public class SmsChannelServiceImpl extends BaseServiceImpl<SmsChannelDo> implements SmsChannelService {

    @Resource
    private SmsChannelDao smsChannelDao;

    @Override
    public SmsChannelDo findSmsChannel(SmsChannelDo query) {
        return smsChannelDao.selectOne(query);
    }

    @Override
    public BaseMapper<SmsChannelDo> getMapper() {
        return smsChannelDao;
    }
}