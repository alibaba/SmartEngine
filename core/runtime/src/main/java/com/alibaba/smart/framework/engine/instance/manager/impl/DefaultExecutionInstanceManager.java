package com.alibaba.smart.framework.engine.instance.manager.impl;

import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.manager.ExecutionInstanceManager;
import com.alibaba.smart.framework.engine.instance.store.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.store.ProcessInstanceStorage;

import java.util.List;
import java.util.UUID;

/**
 * Created by ettear on 16-4-19.
 */
public class DefaultExecutionInstanceManager implements ExecutionInstanceManager,LifeCycleListener {

    private ExtensionPointRegistry   extensionPointRegistry;
    private ProcessInstanceStorage   processInstanceStorage;
    private ExecutionInstanceStorage executionInstanceStorage;

    public DefaultExecutionInstanceManager(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {
        this.processInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ProcessInstanceStorage.class);
        this.executionInstanceStorage=this.extensionPointRegistry.getExtensionPoint(ExecutionInstanceStorage.class);
    }

    @Override
    public void stop() {

    }

    @Override
    public ExecutionInstance create(ExecutionInstance execution) {
        execution.setInstanceId(UUID.randomUUID().toString());
        this.executionInstanceStorage.save(execution);
        return null;
    }

    @Override
    public void updateActivity(String processInstanceId, String executionInstanceId,
                               ActivityInstance activityInstance) {
    }

    @Override
    public void suspend(String processInstanceId, String executionInstanceId, String step) {

    }

    @Override
    public void complete(String processInstanceId, String executionInstanceId) {

    }

    @Override
    public List<ExecutionInstance> findExecutions(String processInstanceId) {
        return null;
    }


}
