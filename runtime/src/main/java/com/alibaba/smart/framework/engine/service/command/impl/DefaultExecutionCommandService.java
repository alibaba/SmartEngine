package com.alibaba.smart.framework.engine.service.command.impl;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.service.TaskAssigneeService;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmProcessInstance;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.common.util.DateUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 高海军 帝奇  2016.11.11
 */
public class DefaultExecutionCommandService implements ExecutionCommandService, LifeCycleListener {

    private ExtensionPointRegistry extensionPointRegistry;
    private ProcessDefinitionContainer processContainer;
    private InstanceContextFactory instanceContextFactory;

//    private ProcessInstanceStorage processInstanceStorage;
//    private ActivityInstanceStorage activityInstanceStorage;
//    private ExecutionInstanceStorage executionInstanceStorage;


    public DefaultExecutionCommandService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {
        this.processContainer = this.extensionPointRegistry.getExtensionPoint(ProcessDefinitionContainer.class);
        this.instanceContextFactory = this.extensionPointRegistry.getExtensionPoint(InstanceContextFactory.class);

//        this.processInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ProcessInstanceStorage.class);
//        this.activityInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ActivityInstanceStorage.class);
//        this.executionInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ExecutionInstanceStorage.class);

    }

    @Override
    public void stop() {

    }

    @Override
    public ProcessInstance signal(Long executionInstanceId, Map<String, Object> request) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        //TUNE 可以在对象创建时初始化,但是这里依赖稍微有点问题
        ProcessInstanceStorage processInstanceStorage =persisterFactoryExtensionPoint.getExtensionPoint(ProcessInstanceStorage.class);
        ActivityInstanceStorage activityInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ActivityInstanceStorage.class);
        ExecutionInstanceStorage executionInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);
        TaskInstanceStorage taskInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);

        ExecutionInstance executionInstance = executionInstanceStorage.find(executionInstanceId);
        ActivityInstance activityInstance= activityInstanceStorage.find(executionInstance.getActivityInstanceId());

        ProcessInstance processInstance = processInstanceStorage.find(executionInstance.getProcessInstanceId());

        PvmProcessDefinition pvmProcessDefinition = this.processContainer.get(executionInstance.getProcessDefinitionIdAndVersion());
        String activityId = executionInstance.getActivityId();
        PvmActivity pvmActivity = pvmProcessDefinition.getActivities().get(activityId);


        ExecutionContext executionContext = this.instanceContextFactory.create();
        executionContext.setExtensionPointRegistry(this.extensionPointRegistry);
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();
        executionContext.setProcessEngineConfiguration(processEngineConfiguration);
        executionContext.setPvmProcessDefinition(pvmProcessDefinition);
        executionContext.setProcessInstance(processInstance);
        executionContext.setRequest(request);
        executionContext.setBlockId(activityInstance.getBlockId());


        //执行每个节点的hook方法
        ActivityBehavior activityBehavior =  pvmActivity.getActivityBehavior();
        activityBehavior.leave(pvmActivity,executionContext);


        // TUNE 减少不必要的对象创建
        PvmProcessInstance pvmProcessInstance = new DefaultPvmProcessInstance();

        markDone(activityInstance,executionInstance);


        ProcessInstance newProcessInstance = pvmProcessInstance.signal(pvmActivity, executionContext);

        persist(newProcessInstance,  request);

        return newProcessInstance;
    }




        private ProcessInstance persist(ProcessInstance processInstance,Map<String, Object> request) {

            PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

            //TUNE 可以在对象创建时初始化,但是这里依赖稍微有点问题
            ProcessInstanceStorage processInstanceStorage =persisterFactoryExtensionPoint.getExtensionPoint(ProcessInstanceStorage.class);
            ActivityInstanceStorage activityInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ActivityInstanceStorage.class);
            ExecutionInstanceStorage executionInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);
            TaskInstanceStorage taskInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);


            ProcessInstance newProcessInstance=   processInstanceStorage.update(processInstance);
            List<ActivityInstance> activityInstances = processInstance.getNewActivityInstances();
            for (ActivityInstance activityInstance : activityInstances) {

                //TUNE 代码有点重复
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


                        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();


                        TaskAssigneeService taskAssigneeService = processEngineConfiguration.getTaskAssigneeService();
                        if(null != taskAssigneeService){
                            taskAssigneeService.persistTaskAssignee(taskInstance,request);
                        }

                        executionInstance.setTaskInstance(taskInstance);
                    }


                }
            }

            return newProcessInstance;
        }


    private  void markDone(ActivityInstance activityInstance,ExecutionInstance executionInstance) {
        Date completeDate = DateUtil.getCurrentDate();
        executionInstance.setCompleteDate(completeDate);
        executionInstance.setActive(false);

        activityInstance.setCompleteDate(completeDate);

        //TODO
        //activityInstance.setActive(false);


        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        ExecutionInstanceStorage executionInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);

        executionInstanceStorage.update(executionInstance);

    }

}
