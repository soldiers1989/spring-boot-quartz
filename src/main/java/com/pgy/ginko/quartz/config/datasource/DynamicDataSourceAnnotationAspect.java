package com.pgy.ginko.quartz.config.datasource;

import com.pgy.ginko.quartz.annotation.DataSource;
import com.pgy.ginko.quartz.common.enums.DataSourceKey;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author ginko
 * @description 切换数据源通过切面触发
 * @date 2018/8/23 15:35
 */
@Slf4j
@Aspect
@Component
public class DynamicDataSourceAnnotationAspect {

    @Before("@annotation(com.pgy.ginko.quartz.annotation.DataSource)")
    public void beforeSwitchDataSource(JoinPoint point) {

        //获得当前访问的class
        Class<?> className = point.getTarget().getClass();

        //获得访问的方法名
        String methodName = point.getSignature().getName();
        //得到方法的参数的类型
        Class[] argClass = ((MethodSignature) point.getSignature()).getParameterTypes();
        DataSourceKey dataSourceType = DataSourceKey.BIZ;
        try {
            // 得到访问的方法对象
            Method method = className.getMethod(methodName, argClass);
            // 判断是否存在@DataSource注解
            if (method.isAnnotationPresent(DataSource.class)) {
                DataSource annotation = method.getAnnotation(DataSource.class);
                // 取出方法注解中的数据源名
                dataSourceType = annotation.value();
            } else if (className.isAnnotationPresent(DataSource.class)) {
                DataSource annotation = className.getAnnotation(DataSource.class);
                // 取出类注解中的数据源名
                dataSourceType = annotation.value();
            }
        } catch (Exception e) {
            log.error("切换数据源通过切面触发异常", e);
        }
        // 切换数据源
        DynamicDataSourceContextHolder.setDataSourceKey(dataSourceType.getName());
    }

    @After("@annotation(com.pgy.ginko.quartz.annotation.DataSource)")
    public void afterSwitchDataSource() {
        DynamicDataSourceContextHolder.clearDataSourceKey();

    }
}
