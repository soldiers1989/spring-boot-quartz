package com.pgy.ginko.quartz.service.biz.impl;

import com.pgy.ginko.quartz.service.biz.BaseService;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * Created by Wangmx on 2018/8/25 12:25.
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    public abstract BaseMapper<T> getMapper();

    @Override
    public int deleteByPrimaryKey(Long rid) {
        return getMapper().deleteByPrimaryKey(rid);
    }

    @Override
    public int delete(T t) {
        return getMapper().delete(t);
    }

    @Override
    public int insert(T t) {
        return getMapper().insert(t);
    }

    @Override
    public int insertSelective(T t) {
        return getMapper().insertSelective(t);
    }

    @Override
    public boolean existsWithPrimaryKey(Long rid) {
        return getMapper().existsWithPrimaryKey(rid);
    }

    @Override
    public List<T> selectAll() {
        return getMapper().selectAll();
    }

    @Override
    public T selectByPrimaryKey(Long rid) {
        return getMapper().selectByPrimaryKey(rid);
    }

    @Override
    public int selectCount(T t) {
        return getMapper().selectCount(t);
    }

    @Override
    public List<T> select(T t) {
        return getMapper().select(t);
    }

    @Override
    public T selectOne(T t) {
        return getMapper().selectOne(t);
    }

    @Override
    public int updateByPrimaryKey(T t) {
        return getMapper().updateByPrimaryKey(t);
    }

    @Override
    public int updateByPrimaryKeySelective(T t) {
        return getMapper().updateByPrimaryKeySelective(t);
    }
}
