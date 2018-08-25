package com.pgy.ginko.quartz.service.biz.impl;


import com.pgy.ginko.quartz.dao.biz.LsdAssetDao;
import com.pgy.ginko.quartz.model.biz.LsdAssetDo;
import com.pgy.ginko.quartz.service.biz.LsdAssetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service("lsdAssetServiceImpl")
public class LsdAssetServiceImpl extends BaseServiceImpl<LsdAssetDo> implements LsdAssetService {

    @Resource
    private LsdAssetDao lsdAssetDao;

    @Override
    public LsdAssetDo findAssetByBorrowIdTypeNoFinished(Long id, Integer code) {
        return lsdAssetDao.getAssetByBorrowIdTypeNoFinished(id, code);
    }


}
