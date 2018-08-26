package com.pgy.ginko.quartz.service.biz.impl;

import com.pgy.ginko.quartz.annotation.DataSource;
import com.pgy.ginko.quartz.common.enums.DataSourceKey;
import com.pgy.ginko.quartz.dao.biz.LsdCommitRecordDao;
import com.pgy.ginko.quartz.model.biz.LsdCommitRecordDo;
import com.pgy.ginko.quartz.service.biz.LsdCommitRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * Created by Wangmx on 2018/8/26 0026.
 */
@Slf4j
@DataSource(DataSourceKey.BIZ)
@Service("lsdCommitRecordService")
public class LsdCommitRecordServiceImpl extends BaseServiceImpl<LsdCommitRecordDo> implements LsdCommitRecordService {

    private LsdCommitRecordDao lsdCommitRecordDao;

    @Override
    public BaseMapper<LsdCommitRecordDo> getMapper() {
        return lsdCommitRecordDao;
    }
}