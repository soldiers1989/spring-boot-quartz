package com.pgy.ginko.quartz.config.datasource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.pgy.ginko.quartz.common.DataSourceKey;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ginko
 * @description 数据源配置类，在该类中生成多个数据源实例并将其注入到 ApplicationContext 中
 * @date 2018/8/23 10:36
 */
@Configuration
public class DataSourceConfig {

    /**
     * biz DataSource
     *
     * @return data source
     * @Primary 注解用于标识默认使用的 DataSource Bean，因为有5个 DataSource Bean，该注解可用于 master
     * 或 slave DataSource Bean, 但不能用于 dynamicDataSource Bean, 否则会产生循环调用
     * @ConfigurationProperties 注解用于从 application.properties 文件中读取配置，为 Bean 设置属性
     */
    @Bean("biz")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.druid.biz")
    public DataSource biz() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * collection data source.
     *
     * @return the data source
     */
    @Bean("collection")
    @ConfigurationProperties(prefix = "spring.datasource.druid.collection")
    public DataSource collection() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * Slave beta data source.
     *
     * @return the data source
     */
    @Bean("risk")
    @ConfigurationProperties(prefix = "spring.datasource.druid.risk")
    public DataSource risk() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * Slave gamma data source.
     *
     * @return the data source
     */
    @Bean("ups")
    @ConfigurationProperties(prefix = "spring.datasource.druid.ups")
    public DataSource ups() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * Slave gamma data source.
     *
     * @return the data source
     */
    @Bean("quartz")
    @Order(0)
    @ConfigurationProperties(prefix = "spring.datasource.druid.quartz")
    public DataSource quartz() {
        return DruidDataSourceBuilder.create().build();
    }


    /**
     * Dynamic data source.
     *
     * @return the data source
     */
    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>(5);
        dataSourceMap.put(DataSourceKey.BIZ.getName(), biz());
        dataSourceMap.put(DataSourceKey.COLLECTION.getName(), collection());
        dataSourceMap.put(DataSourceKey.RISK.getName(), risk());
        dataSourceMap.put(DataSourceKey.UPS.getName(), ups());
        dataSourceMap.put(DataSourceKey.QUARTZ.getName(), quartz());

        // 将 master 数据源作为默认指定的数据源
        dynamicRoutingDataSource.setDefaultTargetDataSource(biz());
        // 将 master 和 slave 数据源作为指定的数据源
        dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);

        // 将数据源的 key 放到数据源上下文的 key 集合中，用于切换时判断数据源是否有效
        DynamicDataSourceContextHolder.dataSourceKeys.addAll(dataSourceMap.keySet());

        // 将 Slave 数据源的 key 放在集合中，用于轮循
        DynamicDataSourceContextHolder.slaveDataSourceKeys.addAll(dataSourceMap.keySet());
//        DynamicDataSourceContextHolder.slaveDataSourceKeys.remove(DataSourceKey.master.getName());
        return dynamicRoutingDataSource;
    }

    /**
     * 配置 SqlSessionFactoryBean
     *
     * @return the sql session factory bean
     * @ConfigurationProperties 在这里是为了将 MyBatis 的 mappers 位置和持久层接口的别名设置到
     * Bean 的属性中，如果没有使用 *.xml 则可以不用该配置，否则将会产生 invalid bond statement 异常
     */
    @Bean
    @ConfigurationProperties(prefix = "mybatis")
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws Exception  {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 配置数据源，此处配置为关键配置，如果没有将 dynamicDataSource 作为数据源则不能实现切换
        sqlSessionFactoryBean.setDataSource(dynamicDataSource());
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mappers/**/*.xml"));
        sqlSessionFactoryBean.setTypeAliasesPackage("com.pgy.ginko.quartz.model");
        return sqlSessionFactoryBean;
    }

    /**
     * 注入 DataSourceTransactionManager 用于事务管理
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }


}
