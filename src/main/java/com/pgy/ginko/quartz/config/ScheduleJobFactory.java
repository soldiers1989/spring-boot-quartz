package com.pgy.ginko.quartz.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author ginko
 */
@Component
public class ScheduleJobFactory extends AdaptableJobFactory {

    @Resource
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    /**
     * For stop value is null in Job class
     *
     * @param bundle
     * @return
     * @throws Exception
     */
    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object jobInstance = super.createJobInstance(bundle);
        autowireCapableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
