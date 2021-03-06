package com.pgy.ginko.quartz.dao.test;

import com.pgy.ginko.quartz.model.test.Product;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * @author ginko
 * @description Product  Mapper
 * @date 2018/8/23 10:46
 */
@Mapper
@Component(value = "productDao")
public interface ProductDao extends BaseMapper<Product> {


}
