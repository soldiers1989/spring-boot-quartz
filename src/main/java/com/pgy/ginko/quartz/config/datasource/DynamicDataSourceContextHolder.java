package com.pgy.ginko.quartz.config.datasource;

import com.pgy.ginko.quartz.common.enums.DataSourceKey;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ginko
 * @description 该类为数据源上下文配置，用于切换数据源
 * @date 2018/8/23 10:38
 */
@Slf4j
public class DynamicDataSourceContextHolder {

    /**
     * 用于在切换数据源时保证不会被其他线程修改
     */
    private static Lock lock = new ReentrantLock();

    /**
     * 用于轮循的计数器
     */
    private static int counter = 0;

    /**
     * 定义ThreadLocal时重写initialValue方法，返回用户想要的值
     * Maintain variable for every thread, to avoid effect other thread
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = ThreadLocal.withInitial(DataSourceKey.BIZ::getName);

    /**
     * All DataSource List
     */
    static List<Object> dataSourceKeys = new ArrayList<>();

    /**
     * The constant slaveDataSourceKeys.
     */
    static List<Object> slaveDataSourceKeys = new ArrayList<>();

    /**
     * To switch DataSource
     *
     * @param key the key
     */
    public static void setDataSourceKey(String key) {
        if (!CONTEXT_HOLDER.get().equals(key)) {
            CONTEXT_HOLDER.set(key);
        }
    }

    /**
     * Use master data source.
     */
    private static void useMasterDataSource() {
        CONTEXT_HOLDER.set(DataSourceKey.BIZ.getName());
    }

    /**
     * 当使用只读数据源时通过轮循方式选择要使用的数据源
     */
    static void useSlaveDataSource() {
        lock.lock();

        try {
            int datasourceKeyIndex = counter % slaveDataSourceKeys.size();
            CONTEXT_HOLDER.set(slaveDataSourceKeys.get(datasourceKeyIndex) == null ? "" : slaveDataSourceKeys.get(datasourceKeyIndex).toString());
            counter++;
        } catch (Exception e) {
            log.error("Switch slave datasource failed, error message is {}", e.getMessage());
            useMasterDataSource();
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 使用定时任务数据源
     */
    static void useSlaveQuartzSource() {
        lock.lock();
        try {
            CONTEXT_HOLDER.set(DataSourceKey.QUARTZ.getName());
        } catch (Exception e) {
            log.error("Switch slave datasource failed, error message is {}", e.getMessage());
            useMasterDataSource();
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Get current DataSource
     *
     * @return data source key
     */
    static String getDataSourceKey() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * To set DataSource as default
     */
    static void clearDataSourceKey() {
        CONTEXT_HOLDER.remove();
    }

    /**
     * Check if give DataSource is in current DataSource list
     *
     * @param key the key
     * @return boolean boolean
     */
    public static boolean containDataSourceKey(String key) {
        return dataSourceKeys.contains(key);
    }


}
