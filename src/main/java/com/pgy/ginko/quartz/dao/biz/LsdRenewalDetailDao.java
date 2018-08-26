package com.pgy.ginko.quartz.dao.biz;

import com.pgy.ginko.quartz.model.biz.LsdRenewalDetailDo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * Created by Wangmx on 2018/8/26 .
 */
@Mapper
@Component(value = "lsdRenewalDetailDao")
public interface LsdRenewalDetailDao extends BaseMapper<LsdRenewalDetailDo> {

    LsdRenewalDetailDo getProcessingRenewalByBorrowId(Long borrowId);
}