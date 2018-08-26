package com.pgy.ginko.quartz.config;

import com.pgy.ginko.quartz.model.test.ScheduleJob;
import com.pgy.ginko.quartz.service.test.JobService;
import com.pgy.ginko.quartz.utils.ScheduleUtil;
import com.pgy.ginko.quartz.utils.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ginko
 */
@Slf4j
@Component
public class ApplicationListener implements CommandLineRunner {

    @Resource
    private JobService jobService;

    @Override
    public void run(String... args) throws Exception {

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // Run schedule job when Application startup
        List<ScheduleJob> scheduleJobList = jobService.getAllEnableJob();
        for (ScheduleJob scheduleJob : scheduleJobList) {
            try {
                CronTrigger cronTrigger = ScheduleUtil.getCronTrigger(scheduler, scheduleJob);
                if (cronTrigger == null) {
                    ScheduleUtil.createScheduleJob(scheduler, scheduleJob);
                } else {
                    ScheduleUtil.updateScheduleJob(scheduler, scheduleJob);
                }
                log.info("Startup {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
    }
}
