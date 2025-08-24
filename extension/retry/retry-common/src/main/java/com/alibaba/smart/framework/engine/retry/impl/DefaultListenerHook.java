package com.alibaba.smart.framework.engine.retry.impl;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.retry.RetryListener;
import com.alibaba.smart.framework.engine.retry.model.instance.RetryRecord;
import com.alibaba.smart.framework.engine.retry.service.command.RetryService;

/**
 * 默认重试消息监听实现
 *
 * @author zhenhong.tzh
 * @date 2019-04-27
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = RetryListener.class)

public class DefaultListenerHook implements RetryListener, LifeCycleHook, ProcessEngineConfigurationAware {

    private RetryService retryService;

    @Override
    public void onMessage(RetryRecord retryRecord) {
        start();
        if (retryService == null) {
            throw new EngineException("Retry service not found.");
        }
        retryService.retry(retryRecord);
    }


    @Override
    public void start() {
        this.retryService = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.COMMON,RetryService.class);
    }

    @Override
    public void stop() {

    }

    private ProcessEngineConfiguration processEngineConfiguration;

    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
}
