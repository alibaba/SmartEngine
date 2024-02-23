package com.alibaba.smart.framework.engine.service.command.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.constant.AdHocConstant;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultVariableInstance;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.VariableInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  20:38.
 */
public abstract  class CommonServiceHelper {

//    @Deprecated
//    public static void tryLock(ProcessEngineConfiguration processEngineConfiguration,
//                               ProcessInstance processInstance) {
//        LockStrategy lockStrategy = processEngineConfiguration.getLockStrategy();
//        if(null != lockStrategy){
//            String bizUniqueId = processInstance.getBizUniqueId();
//
//            if(null != bizUniqueId){
//                lockStrategy.tryLock(bizUniqueId,null);
//            }else {
//                lockStrategy.tryLock(processInstance.getInstanceId(),null);
//            }
//        }
//    }
//
//    @Deprecated
//    public static void tryUnlock(ProcessEngineConfiguration processEngineConfiguration,
//                               ProcessInstance processInstance) {
//        LockStrategy lockStrategy = processEngineConfiguration.getLockStrategy();
//        if(null != lockStrategy){
//            String bizUniqueId = processInstance.getBizUniqueId();
//
//            if(null != bizUniqueId){
//                lockStrategy.unLock(bizUniqueId,null);
//            }else {
//                lockStrategy.unLock(processInstance.getInstanceId(),null);
//            }
//        }
//    }

    public static ProcessInstance insertAndPersist(ProcessInstance processInstance, Map<String, Object> request,
                                                   ProcessEngineConfiguration processEngineConfiguration) {


        //TUNE 可以在对象创建时初始化,但是这里依赖稍微有点问题
        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
        ProcessInstanceStorage processInstanceStorage = annotationScanner.getExtensionPoint(
            ExtensionConstant.COMMON,ProcessInstanceStorage.class);

        ProcessInstance newProcessInstance =  processInstanceStorage.insert(processInstance, processEngineConfiguration);

        persistVariableInstanceIfPossible(request, processEngineConfiguration,
            newProcessInstance, AdHocConstant.DEFAULT_ZERO_VALUE);

        persist(newProcessInstance,processEngineConfiguration);

        return newProcessInstance;
    }

    private static void persistVariableInstanceIfPossible(Map<String, Object> request,
                                                          ProcessEngineConfiguration processEngineConfiguration,
                                                          ProcessInstance newProcessInstance, String executionInstanceId) {
        VariablePersister variablePersister = processEngineConfiguration.getVariablePersister();
        if( variablePersister.isPersisteVariableInstanceEnabled() && null!= request ){
            AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
            VariableInstanceStorage variableInstanceStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,VariableInstanceStorage.class);
            for (Entry<String, Object> entry : request.entrySet()) {
                String key = entry.getKey();

                Set<String> blackList = variablePersister.getBlockList();
                if(null!= blackList && blackList.contains(key)){
                    continue;
                }
                
                
                Object value = entry.getValue();
                Class type = value.getClass();

                VariableInstance variableInstance = new DefaultVariableInstance();
                processEngineConfiguration.getIdGenerator().generate(variableInstance);
                variableInstance.setProcessInstanceId(newProcessInstance.getInstanceId());
                variableInstance.setExecutionInstanceId(executionInstanceId);
                variableInstance.setFieldKey(key);
                variableInstance.setFieldType(type);
                variableInstance.setFieldValue(value);

                variableInstanceStorage.insert(  variablePersister,variableInstance, processEngineConfiguration);

            }

        }
    }

    public static  ProcessInstance createExecution(String executionInstanceId, ProcessInstance processInstance, Map<String, Object> request,
                                                   ProcessEngineConfiguration processEngineConfiguration) {

        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
        ProcessInstanceStorage processInstanceStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,ProcessInstanceStorage.class);

        ProcessInstance newProcessInstance = processInstanceStorage.update(processInstance,processEngineConfiguration );

        persistVariableInstanceIfPossible(request, processEngineConfiguration,
            newProcessInstance,executionInstanceId);

        persist(processInstance   ,  processEngineConfiguration );

        return newProcessInstance;
    }



    private static void persist(ProcessInstance processInstance, ProcessEngineConfiguration processEngineConfiguration ) {

        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
        ActivityInstanceStorage activityInstanceStorage= annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,ActivityInstanceStorage.class);
        ExecutionInstanceStorage executionInstanceStorage=annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,ExecutionInstanceStorage.class);
        TaskInstanceStorage taskInstanceStorage=annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,TaskInstanceStorage.class);
        TaskAssigneeStorage taskAssigneeStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,TaskAssigneeStorage.class);


        List<ActivityInstance> activityInstances = processInstance.getActivityInstances();
        for (ActivityInstance activityInstance : activityInstances) {

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
    
    
	public static void createExecution(ExecutionInstance executionInstance, ProcessEngineConfiguration processEngineConfiguration) {
		AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
		ExecutionInstanceStorage executionInstanceStorage=annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,ExecutionInstanceStorage.class);
		if (null != executionInstance) {
            executionInstanceStorage.insert(executionInstance,processEngineConfiguration );
        }
	}

}
