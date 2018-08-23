package com.pgy.ginko.quartz.dao.job;


import com.pgy.ginko.quartz.annotation.DataSource;
import com.pgy.ginko.quartz.common.DataSourceKey;
import com.pgy.ginko.quartz.model.ScheduleJob;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * @author ginko
 * @date 2018-8-22 13:22:20
 */
@Mapper
@Component(value = "jobDao")
public interface JobDao extends BaseMapper<ScheduleJob> {


    @DataSource(DataSourceKey.slaveQuartz)
    List<ScheduleJob> getAllJob();

    @DataSource(DataSourceKey.slaveQuartz)
    List<ScheduleJob> getAllEnableJob();
}
