package com.pgy.ginko.quartz.service.biz;

import com.pgy.ginko.quartz.model.biz.LsdBorrowCashDo;

import java.util.Date;
import java.util.List;

/**
 * @author ginko
 * @description LsdBorrowCashService
 * @date 2018-8-23 21:17:54
 */
public interface LsdBorrowCashService extends BaseService<LsdBorrowCashDo> {

    Long getBorrowCashOverdueMaxCount();

    List<LsdBorrowCashDo> getBorrowCashOverdueByBorrowId(int beginId, int endId);

    List<Long> getBorrowOverdueFirstDayCount(Date day, int overDay);

    List<Long> getBorrowOverdueAnotherDayCount(Date day);

    LsdBorrowCashDo getBorrowCashById(Long id);

}
