package com.alibaba.smart.framework.engine.persister.custom.service;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.retry.instance.storage.RetryRecordStorage;
import com.alibaba.smart.framework.engine.retry.model.instance.RetryRecord;
import com.alibaba.smart.framework.engine.retry.service.command.RetryPersistence;

/**
 * @author zhenhong.tzh
 * @date 2019-05-27
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = RetryRecordStorage.class)

public class CustomRetryRecordStorage implements RetryRecordStorage {

    @Override
    public RetryRecord find(String instanceId, RetryPersistence retryPersistence) {
        return null;
    }

    @Override
    public boolean insert(RetryRecord retryRecord, RetryPersistence retryPersistence) {
        return false;
    }

    @Override
    public boolean update(RetryRecord retryRecord, RetryPersistence retryPersistence) {
        return false;
    }
}
