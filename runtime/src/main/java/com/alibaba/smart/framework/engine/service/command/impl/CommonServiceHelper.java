package com.alibaba.smart.framework.engine.service.command.impl;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.constant.AdHocConstant;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.impl.DefaultVariableInstance;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.VariableInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  20:38.
 */
public abstract  class CommonServiceHelper {

    public static ProcessInstance insertAndPersist(ProcessInstance processInstance,Map<String, Object> request,ExtensionPointRegistry extensionPointRegistry) {
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();


        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        ProcessInstance newProcessInstance = null;

        //TUNE 可以在对象创建时初始化,但是这里依赖稍微有点问题
        ProcessInstanceStorage processInstanceStorage =persisterFactoryExtensionPoint.getExtensionPoint(ProcessInstanceStorage.class);

        LockStrategy lockStrategy = processEngineConfiguration.getLockStrategy();
        if(null != lockStrategy){
            // 这个时候，流程实例可能已经完成或者终止。
            if(!InstanceStatus.running.equals(processInstance.getStatus())){
                newProcessInstance =  processInstanceStorage.update(processInstance, processEngineConfiguration);
            }else {
                newProcessInstance = processInstance;
            }
        }else{
             newProcessInstance =  processInstanceStorage.insert(processInstance, processEngineConfiguration);
        }


        persisteVariableInstanceIfPossible(request, processEngineConfiguration, persisterFactoryExtensionPoint,
            newProcessInstance, AdHocConstant.DEFAULT_ZERO_VALUE);

        persist(newProcessInstance,    extensionPointRegistry,processEngineConfiguration);

        return newProcessInstance;
    }

    private static void persisteVariableInstanceIfPossible(Map<String, Object> request,
                                                           ProcessEngineConfiguration processEngineConfiguration,
                                                           PersisterFactoryExtensionPoint
                                                               persisterFactoryExtensionPoint,
                                                           ProcessInstance newProcessInstance,String executionInstanceId) {
        VariablePersister variablePersister = processEngineConfiguration.getVariablePersister();
        if( variablePersister.isPersisteVariableInstanceEnabled() && null!= request ){
            VariableInstanceStorage variableInstanceStorage =persisterFactoryExtensionPoint.getExtensionPoint(VariableInstanceStorage.class);
            for (Entry<String, Object> entry : request.entrySet()) {
                String key = entry.getKey();

                Set<String> blackList = variablePersister.getBlackList();
                if(null!= blackList && blackList.contains(key)){
                    continue;
                }
                
                
                Object value = entry.getValue();
                Class type = value.getClass();

                VariableInstance variableInstance = new DefaultVariableInstance();
                variableInstance.setInstanceId(processEngineConfiguration.getIdGenerator().getId());
                variableInstance.setProcessInstanceId(newProcessInstance.getInstanceId());
                variableInstance.setExecutionInstanceId(executionInstanceId);
                variableInstance.setFieldKey(key);
                variableInstance.setFieldType(type);
                variableInstance.setFieldValue(value);

                variableInstanceStorage.insert(  variablePersister,variableInstance, processEngineConfiguration);

            }

        }
    }

    public static  ProcessInstance updateAndPersist(String executionInstanceId,ProcessInstance processInstance,Map<String, Object> request,ExtensionPointRegistry extensionPointRegistry) {
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();

        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        //TUNE 可以在对象创建时初始化,但是这里依赖稍微优化下。
        ProcessInstanceStorage processInstanceStorage =persisterFactoryExtensionPoint.getExtensionPoint(ProcessInstanceStorage.class);

        ProcessInstance newProcessInstance=   processInstanceStorage.update(processInstance,processEngineConfiguration );

        persisteVariableInstanceIfPossible(request, processEngineConfiguration, persisterFactoryExtensionPoint,
            newProcessInstance,executionInstanceId);

        persist(processInstance,    extensionPointRegistry,  processEngineConfiguration );

        return newProcessInstance;
    }



    private static void persist(ProcessInstance processInstance,  ExtensionPointRegistry extensionPointRegistry,ProcessEngineConfiguration processEngineConfiguration ) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        ActivityInstanceStorage activityInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ActivityInstanceStorage.class);
        ExecutionInstanceStorage executionInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);
        TaskInstanceStorage taskInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);
        TaskAssigneeStorage taskAssigneeStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskAssigneeStorage.class);


        List<ActivityInstance> activityInstances = processInstance.getActivityInstances();
        for (ActivityInstance activityInstance : activityInstances) {

            //TUNE 这里重新赋值了,id还是统一由数据库分配.
            activityInstance.setProcessInstanceId(processInstance.getInstanceId());
            activityInstanceStorage.insert(activityInstance,processEngineConfiguration );

            List<ExecutionInstance> executionInstanceList = activityInstance.getExecutionInstanceList();

            if(null != executionInstanceList){
                for (ExecutionInstance executionInstance : executionInstanceList) {
                    persisteInstance( executionInstanceStorage, taskInstanceStorage,taskAssigneeStorage,
                        activityInstance,
                        executionInstance,  processEngineConfiguration);
                }
            }

        }

    }

    //TUNE too many args
    private static void persisteInstance(ExecutionInstanceStorage executionInstanceStorage,
                                         TaskInstanceStorage taskInstanceStorage, TaskAssigneeStorage taskAssigneeStorage, ActivityInstance activityInstance,
                                         ExecutionInstance executionInstance, ProcessEngineConfiguration processEngineConfiguration) {
        if (null != executionInstance) {
            executionInstance.setProcessInstanceId(activityInstance.getProcessInstanceId());
            executionInstance.setActivityInstanceId(activityInstance.getInstanceId());
            executionInstanceStorage.insert(executionInstance,processEngineConfiguration );

            TaskInstance taskInstance = executionInstance.getTaskInstance();
            if(null!= taskInstance) {
                taskInstance.setActivityInstanceId(executionInstance.getActivityInstanceId());
                taskInstance.setProcessInstanceId(executionInstance.getProcessInstanceId());
                taskInstance.setExecutionInstanceId(executionInstance.getInstanceId());

                //reAssign
                taskInstance = taskInstanceStorage.insert(taskInstance,processEngineConfiguration );

                List<TaskAssigneeInstance>  taskAssigneeInstances =  taskInstance.getTaskAssigneeInstanceList();

                if(null != taskAssigneeInstances){
                    for (TaskAssigneeInstance taskAssigneeInstance : taskAssigneeInstances) {
                        taskAssigneeInstance.setTaskInstanceId(taskInstance.getInstanceId());
                        taskAssigneeInstance.setProcessInstanceId(taskInstance.getProcessInstanceId());
                        taskAssigneeStorage.insert(taskAssigneeInstance,processEngineConfiguration );
                    }
                }

                executionInstance.setTaskInstance(taskInstance);
            }


        }
    }

}
