package com.pgy.ginko.quartz.service.biz;

import com.pgy.ginko.quartz.model.biz.LsdResourceDo;

import java.util.List;

/**
 * @author ginko
 * @description LsdResourceService
 * @date 2018-8-23 21:17:54
 */
public interface LsdResourceService extends BaseService<LsdResourceDo> {

    List<LsdResourceDo> getResourceByType(String type);

    LsdResourceDo getResourceByTypeAndSecType(String type, String secType);

}

