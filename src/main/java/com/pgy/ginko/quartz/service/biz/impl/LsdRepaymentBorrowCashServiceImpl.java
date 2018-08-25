package com.pgy.ginko.quartz.service.biz.impl;

import com.pgy.ginko.quartz.dao.biz.LsdRepaymentBorrowCashDao;
import com.pgy.ginko.quartz.model.biz.LsdRepaymentBorrowCashDo;
import com.pgy.ginko.quartz.service.biz.LsdRepaymentBorrowCashService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("lsdRepaymentBorrowCashService")
public class LsdRepaymentBorrowCashServiceImpl extends BaseServiceImpl<LsdRepaymentBorrowCashDo> implements LsdRepaymentBorrowCashService {

    @Resource
    private LsdRepaymentBorrowCashDao lsdRepaymentBorrowCashDao;

    @Override
    public LsdRepaymentBorrowCashDo getProcessingRepaymentByBorrowId(Long borrowId) {
        return lsdRepaymentBorrowCashDao.getProcessingRepaymentByBorrowId(borrowId);
    }

}
