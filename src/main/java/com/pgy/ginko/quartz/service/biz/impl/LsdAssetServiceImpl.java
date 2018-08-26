package com.pgy.ginko.quartz.service.biz.impl;


import com.pgy.ginko.quartz.annotation.DataSource;
import com.pgy.ginko.quartz.common.enums.DataSourceKey;
import com.pgy.ginko.quartz.dao.biz.LsdAssetDao;
import com.pgy.ginko.quartz.model.biz.LsdAssetDo;
import com.pgy.ginko.quartz.service.biz.LsdAssetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.BaseMapper;

import javax.annotation.Resource;

@Slf4j
@DataSource(DataSourceKey.BIZ)
@Service("lsdAssetServiceImpl")
public class LsdAssetServiceImpl extends BaseServiceImpl<LsdAssetDo> implements LsdAssetService {

    @Resource
    private LsdAssetDao lsdAssetDao;

    @Override
    public BaseMapper<LsdAssetDo> getMapper() {
        return lsdAssetDao;
    }

    @Override
    public LsdAssetDo findAssetByBorrowIdTypeNoFinished(Long id, Integer code) {
        return lsdAssetDao.getAssetByBorrowIdTypeNoFinished(id, code);
    }


}
