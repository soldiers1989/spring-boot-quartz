package com.pgy.ginko.quartz.dao.biz;

import com.pgy.ginko.quartz.model.biz.LsdBorrowCashDo;
import com.pgy.ginko.quartz.model.biz.dto.LsdBorrowCashDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    Long getBorrowCashOverdueMaxCount(@Param("startDate") Date startDate);

    List<Long> getBorrowOverdueFirstDayCount(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    List<Long> getBorrowOverdueAnotherDayCount(@Param("nowTime") Date nowTime);

    List<LsdBorrowCashDo> getBorrowCashOverdueByBorrowId(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("beginId") Integer beginId, @Param("endId") Integer endId);

    List<LsdBorrowCashDto> getBorrowListByBorrowIds(@Param("borrowIds") List<Long> borrowIds);
}
