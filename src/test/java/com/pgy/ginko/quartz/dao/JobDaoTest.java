package com.pgy.ginko.quartz.dao;

import com.pgy.ginko.quartz.model.ScheduleJob;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ginko
 * @description ${todo}
 * @date 2018/8/22 19:28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JobDaoTest {

    @Resource
    private JobDao jobDao;

    @Test
    public void testDao() {
        List<ScheduleJob> jobList = jobDao.getAllJob();

        List<ScheduleJob> jobList1 = jobDao.getAllEnableJob();

    }

}