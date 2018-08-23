package com.pgy.ginko.quartz.config;

import com.pgy.ginko.quartz.config.datasource.DynamicRoutingDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static com.pgy.ginko.quartz.common.DataSourceKey.slaveQuartz;

/**
 * @author ginko
 */
@Configuration
public class ScheduleConfig {

    @Resource
    private ScheduleJobFactory jobFactory;



    /**
     * To Configuration Quartz , not necessary, if not config this, will use default
     *
     * @param quartzDataSource
     * @return
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("slaveQuartz") DataSource quartzDataSource) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setSchedulerName("TASK_EXECUTOR");
        schedulerFactoryBean.setStartupDelay(10);
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContextKey");
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setDataSource(quartzDataSource);
        schedulerFactoryBean.setJobFactory(jobFactory);
        return schedulerFactoryBean;
    }


}
