package com.pgy.ginko.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ginko
 */
public class TestJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // Do what you want here
        logger.info("Test job is executing...");
    }
}
