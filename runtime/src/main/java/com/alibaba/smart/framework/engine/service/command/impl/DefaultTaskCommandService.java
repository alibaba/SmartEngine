package com.alibaba.smart.framework.engine.service.command.impl;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;

/**
 * @author 高海军 帝奇  2016.11.11
 */
public class DefaultTaskCommandService implements TaskCommandService, LifeCycleListener {

    private ExtensionPointRegistry extensionPointRegistry;

    private ExecutionCommandService executionCommandService;


    public DefaultTaskCommandService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {
        this.executionCommandService = this.extensionPointRegistry.getExtensionPoint(ExecutionCommandService.class);
    }

    @Override
    public void stop() {

    }


    @Override
    public void complete(Long taskId, Map<String, Object> variables) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(
            PersisterFactoryExtensionPoint.class);

        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(
            TaskInstanceStorage.class);
        TaskInstance taskInstance = taskInstanceStorage.find(taskId);
        MarkDoneUtil.markDoneTaskInstance(taskInstance, TaskInstanceConstant.COMPLETED, TaskInstanceConstant.PENDING,
            variables, taskInstanceStorage);

        executionCommandService.signal(taskInstance.getExecutionInstanceId(), variables);

    }



    @Override
    public void complete(Long taskId, String userId, Map<String, Object> variables) {
        if(null == variables){
            variables = new HashMap<String, Object>();
        }
        variables.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID,userId);

        //TODO check priviage

        complete(  taskId, variables);
    }

    //@Override
    //public void claim(Long taskId, String userId, Map<String, Object> variables) {
    //
    //}
}
