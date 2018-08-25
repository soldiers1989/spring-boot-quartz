package com.pgy.ginko.quartz.service.biz.impl;


import com.pgy.ginko.quartz.dao.biz.LsdBorrowCashDao;
import com.pgy.ginko.quartz.model.biz.LsdBorrowCashDo;
import com.pgy.ginko.quartz.service.biz.LsdBorrowCashService;
import com.pgy.ginko.quartz.utils.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.*;

@Service("lsdBorrowCashService")
public class LsdBorrowCashServiceImpl extends BaseServiceImpl<LsdBorrowCashDo> implements LsdBorrowCashService {

    @Resource
    private LsdBorrowCashDao lsdBorrowCashDao;

    public Long getBorrowCashOverdueMaxCount() {
        Date date = new Date(System.currentTimeMillis());
        Date startDate = DateUtil.getStartOfDate(date);
        return lsdBorrowCashDao.getBorrowCashOverdueMaxCount(startDate);
    }

    public LsdBorrowCashDo getBorrowCashById(Long id) {
        return lsdBorrowCashDao.selectByPrimaryKey(id);
    }


    public List<Long> getBorrowOverdueFirstDayCount(Date day, int overDay) {
        Date beginTime = DateUtil.getStartOfDate(day);
        Date endTime = DateUtil.getStartOfDate(DateUtil.addDays(day, overDay));
        return lsdBorrowCashDao.getBorrowOverdueFirstDayCount(beginTime, endTime);
    }


    public List<Long> getBorrowOverdueAnotherDayCount(Date day) {
        Date nowTime = DateUtil.getStartOfDate(day);

        return lsdBorrowCashDao.getBorrowOverdueAnotherDayCount(nowTime);
    }

    public List<LsdBorrowCashDo> getBorrowCashOverdueByBorrowId(int beginId, int endId) {
        Date date = new Date(System.currentTimeMillis());
        Date startDate = DateUtil.getStartOfDate(date);
        Date endDate = DateUtil.getEndOfDate(date);
        return lsdBorrowCashDao.getBorrowCashOverdueByBorrowId(startDate, endDate, beginId, endId);
    }


}
