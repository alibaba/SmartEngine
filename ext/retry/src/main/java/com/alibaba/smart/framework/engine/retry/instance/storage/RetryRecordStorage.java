package com.alibaba.smart.framework.engine.retry.instance.storage;

import com.alibaba.smart.framework.engine.retry.model.instance.RetryRecord;

/**
 * @author zhenhong.tzh
 * @date 2019-04-30
 */
public interface RetryRecordStorage {
    /**
     * 新增or更新重试对象记录
     *
     * @param retryRecord 重试对象记录
     * @return
     */
    boolean insertOrUpdate(RetryRecord retryRecord);

}
