package com.pgy.ginko.quartz.service.biz.impl;

import com.pgy.ginko.quartz.annotation.DataSource;
import com.pgy.ginko.quartz.common.enums.DataSourceKey;
import com.pgy.ginko.quartz.dao.biz.LsdResourceDao;
import com.pgy.ginko.quartz.model.biz.LsdResourceDo;
import com.pgy.ginko.quartz.service.biz.LsdResourceService;
import com.pgy.ginko.quartz.service.biz.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@DataSource(DataSourceKey.BIZ)
@Service("lsdResourceService")
public class LsdResourceServiceImpl extends BaseServiceImpl<LsdResourceDo> implements LsdResourceService {

    private static String SMS_RESOURCE_CODE_KEY = "SMS_RESOURCE_CODE_";

    @Resource
    private LsdResourceDao lsdResourceDao;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<LsdResourceDo> getResourceByType(String type) {
        return lsdResourceDao.getResourceByType(type);
    }

    @Override
    public LsdResourceDo getResourceByTypeAndSecType(String type, String secType) {
        String cacheKey = SMS_RESOURCE_CODE_KEY + type + secType;
        LsdResourceDo resource = (LsdResourceDo) redisUtil.get(cacheKey);
        if (resource == null) {
            resource = lsdResourceDao.getResourceByTypeAndSecType(type, secType);
            if (resource != null) {
                try {
                    redisUtil.set(cacheKey, resource);
                } catch (Exception e) {
                    log.debug("短信配置缓存异常", e);
                }
            }
        }
        return resource;
    }

}

