package com.pgy.ginko.quartz.service;

import com.pgy.ginko.quartz.annotation.DataSource;
import com.pgy.ginko.quartz.common.DataSourceKey;
import com.pgy.ginko.quartz.dao.UserDao;
import com.pgy.ginko.quartz.model.User;
import com.pgy.ginko.quartz.utils.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ginko
 * @description ProductService
 * @date 2018/8/23 10:45
 */
@Service
@Slf4j
public class UserService {

    @Resource
    private UserDao userDao;

    @DataSource(DataSourceKey.slaveAlpha)
    public User select(long userId) throws ServiceException {
        User user = userDao.selectByPrimaryKey(userId);
        if (user == null) {
            throw new ServiceException("User:" + userId + " not found");
        }
        return user;
    }

    @DataSource(DataSourceKey.slaveAlpha)
    public List<User> getAllUser() {
        return userDao.selectAll();
    }
}