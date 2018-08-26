package com.pgy.ginko.quartz.dao.biz;

import com.pgy.ginko.quartz.model.biz.LsdCommitRecordDo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * Created by Wangmx on 2018/8/26 .
 */
@Mapper
@Component(value = "lsdCommitRecordDao")
public interface LsdCommitRecordDao extends BaseMapper<LsdCommitRecordDo> {


}