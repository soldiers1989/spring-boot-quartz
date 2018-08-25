package com.pgy.ginko.quartz.service.biz;

import com.pgy.ginko.quartz.model.biz.LsdAssetDo;

/**
 * @author ginko
 * @description LsdAssetService
 * @date 2018-8-23 21:17:54
 */
public interface LsdAssetService extends BaseService<LsdAssetDo>{

    LsdAssetDo findAssetByBorrowIdTypeNoFinished(Long id, Integer code);

}