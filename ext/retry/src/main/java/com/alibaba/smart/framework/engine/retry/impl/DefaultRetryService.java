package com.alibaba.smart.framework.engine.retry.impl;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
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
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = RetryService.class)
public class DefaultRetryService implements RetryService, LifeCycleHook , ProcessEngineConfigurationAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRetryService.class);

    private static final int MAX_RETRY_TIMES = 3;
    private RetryRecordStorage retryRecordStorage;
    private RetryPersistence retryPersistence;
    private ExecutionCommandService executionCommandService;


    private ProcessEngineConfiguration processEngineConfiguration;

    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }

    @Override
    public void start() {
        if (executionCommandService == null) {
            executionCommandService = processEngineConfiguration.getSmartEngine().getExecutionCommandService();
        }
        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
        if (retryRecordStorage == null) {

            retryRecordStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,RetryRecordStorage.class);
        }
        if (retryPersistence == null) {
            retryPersistence = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,RetryPersistence.class);
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
