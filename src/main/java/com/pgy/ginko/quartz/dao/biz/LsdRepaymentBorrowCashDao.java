package com.pgy.ginko.quartz.dao.biz;

import com.pgy.ginko.quartz.model.biz.LsdRepaymentBorrowCashDo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * Created by Wangmx on 2018/8/26 0026.
 */
@Mapper
@Component(value = "lsdRepaymentBorrowCashDao")
public interface LsdRepaymentBorrowCashDao extends BaseMapper<LsdRepaymentBorrowCashDo> {

    LsdRepaymentBorrowCashDo getProcessingRepaymentByBorrowId(Long borrowId);
}
