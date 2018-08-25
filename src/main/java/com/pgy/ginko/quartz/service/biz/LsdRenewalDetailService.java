package com.pgy.ginko.quartz.service.biz;

import com.pgy.ginko.quartz.model.biz.LsdRenewalDetailDo;

/**
 * @author ginko
 * @description LsdRenewalDetailService
 * @date 2018-8-23 21:17:54
 */
public interface LsdRenewalDetailService extends BaseService<LsdRenewalDetailDo> {

    LsdRenewalDetailDo getProcessingRenewalByBorrowId(Long borrowId);


}
