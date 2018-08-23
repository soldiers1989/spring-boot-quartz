package com.pgy.ginko.quartz.config;

import com.pgy.ginko.quartz.config.datasource.DynamicRoutingDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
     * Dynamic data source.
     *
     * @return the data source
     */
    @Bean("quartzDataSource")
    public DataSource quartzDataSource(@Qualifier("slaveQuartz") DataSource quartzDataSource) {
        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>(1);
        dataSourceMap.put(slaveQuartz.getName(), quartzDataSource);
        // 将slaveQuartz数据源作为指定的数据源
        dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);

        return dynamicRoutingDataSource;
    }

    /**
     * To Configuration Quartz , not necessary, if not config this, will use default
     *
     * @param dataSource
     * @return
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("quartzDataSource") DataSource dataSource) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setSchedulerName("TASK_EXECUTOR");
        schedulerFactoryBean.setStartupDelay(10);
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContextKey");
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setDataSource(dataSource);
        // Make Spring manage instance of Job
        schedulerFactoryBean.setJobFactory(jobFactory);
        return schedulerFactoryBean;
    }


}
