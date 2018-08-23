package com.pgy.ginko.quartz.dao;

import com.pgy.ginko.quartz.annotation.DataSource;
import com.pgy.ginko.quartz.common.DataSourceKey;
import com.pgy.ginko.quartz.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * @author ginko
 * @description User  Mapper
 * @date 2018/8/23 10:46
 */
@Mapper
@Component(value = "userDao")
public interface UserDao extends BaseMapper<User> {


}
