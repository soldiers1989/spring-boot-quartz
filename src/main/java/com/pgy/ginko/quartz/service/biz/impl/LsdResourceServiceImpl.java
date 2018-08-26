package com.pgy.ginko.quartz.service.biz.impl;

import com.pgy.ginko.quartz.annotation.DataSource;
import com.pgy.ginko.quartz.common.enums.DataSourceKey;
import com.pgy.ginko.quartz.dao.biz.LsdResourceDao;
import com.pgy.ginko.quartz.model.biz.LsdResourceDo;
import com.pgy.ginko.quartz.service.biz.LsdResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.BaseMapper;

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
    private RedisService redisService;

    @Override
    public BaseMapper<LsdResourceDo> getMapper() {
        return lsdResourceDao;
    }

    @Override
    public List<LsdResourceDo> getResourceByType(String type) {
        return lsdResourceDao.getResourceByType(type);
    }

    @Override
    public LsdResourceDo getResourceByTypeAndSecType(String type, String secType) {
        String cacheKey = SMS_RESOURCE_CODE_KEY + type + secType;
        LsdResourceDo resource = (LsdResourceDo) redisService.get(cacheKey);
        if (resource == null) {
            resource = lsdResourceDao.getResourceByTypeAndSecType(type, secType);
            if (resource != null) {
                try {
                    redisService.set(cacheKey, resource);
                } catch (Exception e) {
                    log.debug("短信配置缓存异常", e);
                }
            }
        }
        return resource;
    }

}

