package com.pgy.ginko.quartz.service.biz;

import com.pgy.ginko.quartz.model.biz.LsdRepaymentBorrowCashDo;

/**
 * @author ginko
 * @description LsdRepaymentBorrowCashService
 * @date 2018-8-23 21:17:54
 */
public interface LsdRepaymentBorrowCashService extends BaseService<LsdRepaymentBorrowCashDo> {

    LsdRepaymentBorrowCashDo getProcessingRepaymentByBorrowId(Long borrowId);

}
