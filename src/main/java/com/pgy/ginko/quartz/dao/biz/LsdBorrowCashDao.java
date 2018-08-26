package com.pgy.ginko.quartz.dao.biz;

import com.pgy.ginko.quartz.model.biz.LsdBorrowCashDo;
import com.pgy.ginko.quartz.model.biz.dto.LsdBorrowCashDto;
import com.pgy.ginko.quartz.model.test.ScheduleJob;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.Date;
import java.util.List;

/**
 * Created by Wangmx on 2018/8/26.
 */
@Mapper
@Component(value = "lsdBorrowCashDao")
public interface LsdBorrowCashDao extends BaseMapper<LsdBorrowCashDo> {

    Long getBorrowCashOverdueMaxCount(Date startDate);

    List<Long> getBorrowOverdueFirstDayCount(Date beginTime, Date endTime);

    List<Long> getBorrowOverdueAnotherDayCount(Date nowTime);

    List<LsdBorrowCashDo> getBorrowCashOverdueByBorrowId(Date startDate, Date endDate, Integer beginId, Integer endId);

    List<LsdBorrowCashDto> getBorrowListByBorrowIds(List<Long> borrowIds);
}
