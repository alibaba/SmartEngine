package com.alibaba.smart.framework.engine.retry.impl;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.retry.RetryExtensionPoint;
import com.alibaba.smart.framework.engine.retry.instance.storage.RetryRecordStorage;
import com.alibaba.smart.framework.engine.retry.model.instance.RetryRecord;
import com.alibaba.smart.framework.engine.retry.service.command.RetryPersistence;
import com.alibaba.smart.framework.engine.retry.service.command.RetryService;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 重试服务默认实现
 *
 * @author zhenhong.tzh
 * @date 2019-04-30
 */
public class DefaultRetryService implements RetryService, LifeCycleListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRetryService.class);

    private static final int MAX_RETRY_TIMES = 3;
    private ExtensionPointRegistry extensionPointRegistry;
    private RetryRecordStorage retryRecordStorage;
    private RetryPersistence retryPersistence;
    private ExecutionCommandService executionCommandService;

    public DefaultRetryService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {
        if (executionCommandService == null) {
            executionCommandService = this.extensionPointRegistry.getExtensionPoint(ExecutionCommandService.class);
        }
        if (retryRecordStorage == null) {
            PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry
                .getExtensionPoint(PersisterFactoryExtensionPoint.class);
            retryRecordStorage = persisterFactoryExtensionPoint.getExtensionPoint(RetryRecordStorage.class);
        }
        if (retryPersistence == null) {
            RetryExtensionPoint retryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(
                RetryExtensionPoint.class);
            retryPersistence = retryExtensionPoint.getExtensionPoint(RetryPersistence.class);
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean save(RetryRecord retryRecord) {
        start();
        return retryRecordStorage.insert(retryRecord, retryPersistence);
    }

    @Override
    public ProcessInstance retry(RetryRecord retryRecord) {
        start();
        if (retryRecord.getRetryTimes() >= MAX_RETRY_TIMES) {
            LOGGER.warn("Retry too many times, give up.");
            return null;
        }
        ProcessInstance processInstance = null;
        try {
            processInstance = executionCommandService.signal(retryRecord.getInstanceId(),
                retryRecord.getRequestParams());
            retryRecord.setRetrySuccess(true);
        } catch (Exception e) {
            LOGGER.error("Retry process failed, retryRecord : " + retryRecord, e);
            retryRecord.setRetrySuccess(false);
        } finally {
            retryRecord.setRetryTimes(retryRecord.getRetryTimes() + 1);
            retryRecordStorage.update(retryRecord, retryPersistence);
        }
        return processInstance;
    }
}
