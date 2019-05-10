package com.alibaba.smart.framework.engine.retry.instance.storage;

import com.alibaba.smart.framework.engine.retry.model.instance.RetryRecord;

/**
 * @author zhenhong.tzh
 * @date 2019-04-30
 */
public interface RetryRecordStorage {
    boolean insertOrUpdate(RetryRecord retryRecord);

}
