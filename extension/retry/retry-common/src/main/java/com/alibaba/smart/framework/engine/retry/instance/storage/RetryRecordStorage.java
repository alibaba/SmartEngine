package com.alibaba.smart.framework.engine.retry.instance.storage;

import com.alibaba.smart.framework.engine.retry.model.instance.RetryRecord;
import com.alibaba.smart.framework.engine.retry.service.command.RetryPersistence;

/**
 * @author zhenhong.tzh
 * @date 2019-04-30
 */
public interface RetryRecordStorage {
    
    /**
     * 查询记录
     *
     * @param instanceId       instanceId
     * @param retryPersistence 持久化处理
     * @return
     */
    RetryRecord find(String instanceId, RetryPersistence retryPersistence);

    /**
     * 新增重试对象记录
     *
     * @param retryRecord      重试对象记录
     * @param retryPersistence 持久化处理
     * @return
     */
    boolean insert(RetryRecord retryRecord, RetryPersistence retryPersistence);

    /**
     * 更新重试对象记录
     *
     * @param retryRecord      重试对象记录
     * @param retryPersistence 持久化处理
     * @return
     */
    boolean update(RetryRecord retryRecord, RetryPersistence retryPersistence);

}
