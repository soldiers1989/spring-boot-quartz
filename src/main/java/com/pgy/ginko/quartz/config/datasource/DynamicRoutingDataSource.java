package com.pgy.ginko.quartz.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author ginko
 * @description ${todo}
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