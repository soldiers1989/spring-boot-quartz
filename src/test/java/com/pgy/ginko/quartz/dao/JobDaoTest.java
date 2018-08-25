package com.pgy.ginko.quartz.dao;

import com.pgy.ginko.quartz.dao.job.JobDao;
import com.pgy.ginko.quartz.model.test.ScheduleJob;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ginko
 * @description test Dao
 * @date 2018/8/22 19:28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class JobDaoTest {

    @Resource
    private JobDao jobDao;

    @Test
    public void testDao() {
        List<ScheduleJob> jobList = jobDao.getAllJob();

        List<ScheduleJob> jobList1 = jobDao.getAllEnableJob();

        log.info("test Dao");

    }

}