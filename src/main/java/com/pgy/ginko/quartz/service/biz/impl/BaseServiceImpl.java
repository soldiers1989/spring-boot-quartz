package com.pgy.ginko.quartz.service.biz.impl;

import com.pgy.ginko.quartz.service.biz.BaseService;
import tk.mybatis.mapper.common.BaseMapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Wangmx on 2018/8/25 12:25.
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    @Resource
    private BaseMapper<T> baseMapper;

    public abstract BaseMapper<T> getMapper();

    @Override
    public int deleteByPrimaryKey(Long rid) {
        return baseMapper.deleteByPrimaryKey(rid);
    }

    @Override
    public int delete(T t) {
        return baseMapper.delete(t);
    }

    @Override
    public int insert(T t) {
        return baseMapper.insert(t);
    }

    @Override
    public int insertSelective(T t) {
        return baseMapper.insertSelective(t);
    }

    @Override
    public boolean existsWithPrimaryKey(Long rid) {
        return baseMapper.existsWithPrimaryKey(rid);
    }

    @Override
    public List<T> selectAll() {
        return baseMapper.selectAll();
    }

    @Override
    public T selectByPrimaryKey(Long rid) {
        return baseMapper.selectByPrimaryKey(rid);
    }

    @Override
    public int selectCount(T t) {
        return baseMapper.selectCount(t);
    }

    @Override
    public List<T> select(T t) {
        return baseMapper.select(t);
    }

    @Override
    public T selectOne(T t) {
        return baseMapper.selectOne(t);
    }

    @Override
    public int updateByPrimaryKey(T t) {
        return baseMapper.updateByPrimaryKey(t);
    }

    @Override
    public int updateByPrimaryKeySelective(T t) {
        return baseMapper.updateByPrimaryKeySelective(t);
    }
}
