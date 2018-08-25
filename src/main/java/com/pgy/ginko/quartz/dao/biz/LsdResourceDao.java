package com.pgy.ginko.quartz.dao.biz;

import com.pgy.ginko.quartz.model.biz.LsdResourceDo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * Created by Wangmx on 2018/8/26 .
 */
@Mapper
@Component(value = "lsdResourceDao")
public interface LsdResourceDao extends BaseMapper<LsdResourceDo> {

    List<LsdResourceDo> getResourceByType(String type);

    LsdResourceDo getResourceByTypeAndSecType(String type, String secType);

}