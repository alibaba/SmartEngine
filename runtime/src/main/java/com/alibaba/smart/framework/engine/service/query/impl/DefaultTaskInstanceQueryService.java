package com.alibaba.smart.framework.engine.service.query.impl;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.query.TaskInstanceQueryService;

import java.util.List;

/**
 * Created by 高海军 帝奇 74394 on 2016 November  22:10.
 */
public class DefaultTaskInstanceQueryService implements TaskInstanceQueryService, LifeCycleListener {

    private ExtensionPointRegistry extensionPointRegistry;
//    private TaskInstanceStorage taskInstanceStorage;


    public DefaultTaskInstanceQueryService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {

//        this.taskInstanceStorage = this.extensionPointRegistry.getExtensionPoint(TaskInstanceStorage.class);

    }

    @Override
    public void stop() {

    }

    @Override
    public List<TaskInstance> findPendingTask(Long processInstanceId) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);

        return taskInstanceStorage.findPendingTask(processInstanceId);
    }
}
