package com.alibaba.smart.framework.engine.service.command.impl;

import com.alibaba.smart.framework.engine.common.service.TaskAssigneeService;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;

import java.util.List;
import java.util.Map;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  20:38.
 */
public abstract  class CommonServiceHelper {
    public static void persist(ProcessInstance processInstance, Map<String, Object> request,
                               ProcessEngineConfiguration processEngineConfiguration, PersisterFactoryExtensionPoint persisterFactoryExtensionPoint) {

        ActivityInstanceStorage activityInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ActivityInstanceStorage.class);
        ExecutionInstanceStorage executionInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);
        TaskInstanceStorage taskInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);


        List<ActivityInstance> activityInstances = processInstance.getNewActivityInstances();
        for (ActivityInstance activityInstance : activityInstances) {

            //TUNE 这里重新赋值了,id还是统一由数据库分配.
            activityInstance.setProcessInstanceId(processInstance.getInstanceId());
            activityInstanceStorage.insert(activityInstance);

            ExecutionInstance executionInstance = activityInstance.getExecutionInstance();
            if (null != executionInstance) {
                executionInstance.setProcessInstanceId(activityInstance.getProcessInstanceId());
                executionInstance.setActivityInstanceId(activityInstance.getInstanceId());
                executionInstanceStorage.insert(executionInstance);

                TaskInstance taskInstance = executionInstance.getTaskInstance();
                if(null!= taskInstance) {
                    taskInstance.setActivityInstanceId(executionInstance.getActivityInstanceId());
                    taskInstance.setProcessInstanceId(executionInstance.getProcessInstanceId());
                    taskInstance.setExecutionInstanceId(executionInstance.getInstanceId());

                    //reAssign
                    taskInstance = taskInstanceStorage.insert(taskInstance);



                    TaskAssigneeService taskAssigneeService = processEngineConfiguration.getTaskAssigneeService();
                    if(null != taskAssigneeService){
                        taskAssigneeService.persistTaskAssignee(taskInstance,request);
                    }


                    executionInstance.setTaskInstance(taskInstance);
                }


            }
        }
    }

}
