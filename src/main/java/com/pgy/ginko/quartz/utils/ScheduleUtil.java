package com.pgy.ginko.quartz.utils;

import com.pgy.ginko.quartz.model.test.ScheduleJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

/**
 * @author ginko
 * @description Schedule utils for manage Quartz Job
 */
@Slf4j
public class ScheduleUtil {

    /**
     * Get Trigger key
     *
     * @param scheduleJob
     * @return
     */
    public static TriggerKey getTriggerKey(ScheduleJob scheduleJob) {
        return TriggerKey.triggerKey(scheduleJob.getTriggerName(), scheduleJob.getTriggerGroup());
    }

    /**
     * Get Job Key
     *
     * @param scheduleJob
     * @return
     */
    public static JobKey getJobKey(ScheduleJob scheduleJob) {
        return JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
    }

    /**
     * Get Cron Expression trigger
     *
     * @param scheduler
     * @param scheduleJob
     * @return
     * @throws ServiceException
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, ScheduleJob scheduleJob) throws ServiceException {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(scheduleJob));
        } catch (SchedulerException e) {
            throw new ServiceException("Get Cron trigger failed", e);
        }
    }

    /**
     * Create schedule job
     *
     * @param scheduler
     * @param scheduleJob
     * @throws ServiceException
     */
    @SuppressWarnings("unchecked")
    public static void createScheduleJob(Scheduler scheduler, ScheduleJob scheduleJob) throws ServiceException {

        validateCronExpression(scheduleJob);

        try {
            // Get Job class from ScheduleJob entity
            Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(scheduleJob.getClassName()).newInstance().getClass();

            // Create JobDetail entity
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
                    .withDescription(scheduleJob.getDescription())
                    .build();

            // Create CronScheduleBuilder entity
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();

            // Create a new CronTrigger by Cron Expression
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(scheduleJob.getTriggerName(), scheduleJob.getTriggerGroup())
                    .withDescription(scheduleJob.getDescription())
                    .withSchedule(scheduleBuilder)
                    .startNow()
                    .build();

            scheduler.scheduleJob(jobDetail, cronTrigger);

            log.info("Create schedule job {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());

            if (scheduleJob.getPause()) {
                pauseJob(scheduler, scheduleJob);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Execute schedule job failed");
            throw new ServiceException("Execute schedule job failed", e);
        }
    }


    /**
     * Update schedule job
     *
     * @param scheduler
     * @param scheduleJob
     * @throws ServiceException
     */
    public static void updateScheduleJob(Scheduler scheduler, ScheduleJob scheduleJob) throws ServiceException {

        validateCronExpression(scheduleJob);

        try {

            TriggerKey triggerKey = getTriggerKey(scheduleJob);

            // Create CronScheduleBuilder entity
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();

            CronTrigger cronTrigger = getCronTrigger(scheduler, scheduleJob);

            // Create new cron trigger
            cronTrigger = cronTrigger.getTriggerBuilder()
                    .withIdentity(triggerKey)
                    .withDescription(scheduleJob.getDescription())
                    .withSchedule(cronScheduleBuilder).build();

            scheduler.rescheduleJob(triggerKey, cronTrigger);

            log.info("Update schedule job {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());

            if (scheduleJob.getPause()) {
                pauseJob(scheduler, scheduleJob);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("Update schedule job failed");
            throw new ServiceException("Update schedule job failed", e);
        }
    }

    /**
     * Run schedule job
     *
     * @param scheduler
     * @param scheduleJob
     * @throws ServiceException
     */
    public static void run(Scheduler scheduler, ScheduleJob scheduleJob) throws ServiceException {
        try {
            scheduler.triggerJob(getJobKey(scheduleJob));
            log.info("Run schedule job {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("Run schedule job failed");
            throw new ServiceException("Run schedule job failed", e);
        }
    }

    /**
     * Pause schedule job
     *
     * @param scheduler
     * @param scheduleJob
     */
    public static void pauseJob(Scheduler scheduler, ScheduleJob scheduleJob) throws ServiceException {
        try {
            scheduler.pauseJob(getJobKey(scheduleJob));
            log.info("Pause schedule job {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("Pause schedule job failed");
            throw new ServiceException("Pause job failed", e);
        }
    }

    /**
     * Resume schedule job
     *
     * @param scheduler
     * @param scheduleJob
     * @throws ServiceException
     */
    public static void resumeJob(Scheduler scheduler, ScheduleJob scheduleJob) throws ServiceException {
        try {
            scheduler.resumeJob(getJobKey(scheduleJob));
            log.info("Resume schedule job {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("Resume schedule job failed");
            throw new ServiceException("Resume job failed", e);
        }
    }

    /**
     * Delete schedule job
     *
     * @param scheduler
     * @param scheduleJob
     * @throws ServiceException
     */
    public static void deleteJob(Scheduler scheduler, ScheduleJob scheduleJob) throws ServiceException {
        try {
            scheduler.deleteJob(getJobKey(scheduleJob));
            log.info("Delete schedule job {}-{} success", scheduleJob.getJobGroup(), scheduleJob.getJobName());
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("Delete schedule job failed");
            throw new ServiceException("Delete job failed", e);
        }
    }

    /**
     * Validate Cron express
     *
     * @param scheduleJob
     * @throws ServiceException
     */
    public static void validateCronExpression(ScheduleJob scheduleJob) throws ServiceException {
        if (!CronExpression.isValidExpression(scheduleJob.getCronExpression())) {
            throw new ServiceException(String.format("Job %s expression %s is not correct!", scheduleJob.getClassName(), scheduleJob.getCronExpression()));
        }
    }
}
