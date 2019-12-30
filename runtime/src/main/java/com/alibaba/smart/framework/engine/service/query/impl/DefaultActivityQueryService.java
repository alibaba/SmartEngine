package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.service.query.ActivityQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2016 November  22:10.
 */
@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = ActivityQueryService.class)
public class DefaultActivityQueryService implements ActivityQueryService, LifeCycleHook ,
    ProcessEngineConfigurationAware {

    private ActivityInstanceStorage activityInstanceStorage;

    @Override
    public void start() {
        this.activityInstanceStorage = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.COMMON,ActivityInstanceStorage.class);

    }

    @Override
    public void stop() {

    }


    @Override
    public List<ActivityInstance> findAll(String processInstanceId) {

        return activityInstanceStorage.findAll(processInstanceId, processEngineConfiguration);
    }

    private ProcessEngineConfiguration processEngineConfiguration;

    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
}
