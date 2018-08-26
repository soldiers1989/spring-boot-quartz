package com.pgy.ginko.quartz.service.biz;

import com.pgy.ginko.quartz.model.biz.LsdBorrowCashDo;
import com.pgy.ginko.quartz.model.biz.dto.LsdBorrowCashDto;

import java.util.Date;
import java.util.List;

/**
 * @author ginko
 * @description LsdBorrowCashService
 * @date 2018-8-23 21:17:54
 */
public interface LsdBorrowCashService extends BaseService<LsdBorrowCashDo> {


    Long getBorrowCashOverdueMaxCount();

    LsdBorrowCashDo getBorrowCashById(Long id);

    List<Long> getBorrowOverdueFirstDayCount(Date day, int overDay);

    List<Long> getBorrowOverdueAnotherDayCount(Date day);

    List<LsdBorrowCashDo> getBorrowCashOverdueByBorrowId(int beginId, int endId);

    List<LsdBorrowCashDto> getBorrowListByBorrowIds(List<Long> borrowIds);

    List<LsdBorrowCashDto> getBorrowUpdateListByBorrowIds(List<Long> borrowIds);
}
