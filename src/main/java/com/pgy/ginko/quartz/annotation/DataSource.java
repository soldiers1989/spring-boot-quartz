package com.pgy.ginko.quartz.annotation;

import com.pgy.ginko.quartz.common.DataSourceKey;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ginko
 * @description 数据源注解
 * @date 2018/8/23 15:30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DataSource {

    DataSourceKey value() default DataSourceKey.master;

}