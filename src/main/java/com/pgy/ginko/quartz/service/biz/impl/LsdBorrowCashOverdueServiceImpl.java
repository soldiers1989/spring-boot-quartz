package com.pgy.ginko.quartz.service.biz.impl;


import com.pgy.ginko.quartz.annotation.DataSource;
import com.pgy.ginko.quartz.common.enums.DataSourceKey;
import com.pgy.ginko.quartz.model.biz.LsdBorrowCashOverdueDo;
import com.pgy.ginko.quartz.service.biz.LsdBorrowCashOverdueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@DataSource(DataSourceKey.BIZ)
@Service("lsdBorrowCashOverdueService")
public class LsdBorrowCashOverdueServiceImpl extends BaseServiceImpl<LsdBorrowCashOverdueDo> implements LsdBorrowCashOverdueService {


    @Override
    public LsdBorrowCashOverdueDo queryOneByBorrowCashIdOrderByGmtCreateDesc(Long rid) {
        return null;
    }
}
