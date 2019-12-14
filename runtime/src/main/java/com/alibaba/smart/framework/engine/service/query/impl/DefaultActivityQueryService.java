package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.query.ActivityQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2016 November  22:10.
 */
public class DefaultActivityQueryService implements ActivityQueryService, LifeCycleHook {

    private ExtensionPointRegistry extensionPointRegistry;
//    private ActivityInstanceStorage activityInstanceStorage;


    public DefaultActivityQueryService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {

//        this.activityInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ActivityInstanceStorage.class);

    }

    @Override
    public void stop() {

    }


    @Override
    public List<ActivityInstance> findAll(String processInstanceId) {
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(
            SmartEngine.class).getProcessEngineConfiguration();

        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        ActivityInstanceStorage  activityInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(ActivityInstanceStorage.class);

        return activityInstanceStorage.findAll(processInstanceId, processEngineConfiguration);
    }

}
