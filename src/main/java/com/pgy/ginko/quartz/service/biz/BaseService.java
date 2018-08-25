package com.pgy.ginko.quartz.service.biz;

import java.util.List;

/**
 * @author ginko
 * @description BaseService
 * @date 2018/8/23 21:44
 */
public interface BaseService<T> {

    int deleteByPrimaryKey(Long rid);

    int delete(T t);

    int insert(T t);

    int insertSelective(T t);

    boolean existsWithPrimaryKey(Long rid);

    List<T> selectAll();

    T selectByPrimaryKey(Long rid);

    int selectCount(T t);

    List<T> select(T t);

    T selectOne(T t);

    int updateByPrimaryKey(T t);

    int updateByPrimaryKeySelective(T t);
}
