package com.pgy.ginko.quartz.job.biz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by Wangmx on 2018/8/26 12:26.
 */
@Slf4j
public class BorrowCashOverdueSyncCollectionJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {


        log.info("BorrowCashOverdueSyncCollectionJob job is executing...");
    }

}
