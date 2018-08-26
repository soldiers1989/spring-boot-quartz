package com.pgy.ginko.quartz.service.biz;

import com.pgy.ginko.quartz.model.biz.LsdBorrowCashOverdueDo;

/**
 * @author ginko
 * @description LsdBorrowCashOverdueService
 * @date 2018-8-23 21:17:54
 */
public interface LsdBorrowCashOverdueService extends BaseService<LsdBorrowCashOverdueDo> {

    LsdBorrowCashOverdueDo queryOneByBorrowCashIdOrderByGmtCreateDesc(Long rid);
}
