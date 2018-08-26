package com.pgy.ginko.quartz.service.biz.impl;

import com.pgy.ginko.quartz.annotation.DataSource;
import com.pgy.ginko.quartz.common.enums.DataSourceKey;
import com.pgy.ginko.quartz.dao.biz.LsdRepaymentBorrowCashDao;
import com.pgy.ginko.quartz.model.biz.LsdRepaymentBorrowCashDo;
import com.pgy.ginko.quartz.service.biz.LsdRepaymentBorrowCashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.BaseMapper;

import javax.annotation.Resource;

@Slf4j
@DataSource(DataSourceKey.BIZ)
@Service("lsdRepaymentBorrowCashService")
public class LsdRepaymentBorrowCashServiceImpl extends BaseServiceImpl<LsdRepaymentBorrowCashDo> implements LsdRepaymentBorrowCashService {

    @Resource
    private LsdRepaymentBorrowCashDao lsdRepaymentBorrowCashDao;

    @Override
    public BaseMapper<LsdRepaymentBorrowCashDo> getMapper() {
        return lsdRepaymentBorrowCashDao;
    }

    @Override
    public LsdRepaymentBorrowCashDo getProcessingRepaymentByBorrowId(Long borrowId) {
        return lsdRepaymentBorrowCashDao.getProcessingRepaymentByBorrowId(borrowId);
    }


}
