package com.pgy.ginko.quartz.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author ginko
 * @description 在访问数据库时会调用该类的 determineCurrentLookupKey() 方法获取数据库实例的 key
 * @date 2018/8/23 10:35
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {

        log.info("Current DataSource is [{}]", DynamicDataSourceContextHolder.getDataSourceKey());

        return DynamicDataSourceContextHolder.getDataSourceKey();
    }
}