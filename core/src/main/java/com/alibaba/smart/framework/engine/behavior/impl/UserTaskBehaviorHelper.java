package com.alibaba.smart.framework.engine.behavior.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.bpmn.assembly.task.UserTask;
import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.TaskAssigneeDispatcher;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.TaskInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskAssigneeInstance;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

/**
 * Created by 高海军 帝奇 74394 on  2020-03-16 00:19.
 */
public class UserTaskBehaviorHelper {

    static List<TaskAssigneeCandidateInstance> getTaskAssigneeCandidateInstances(ExecutionContext context,
                                                                                  UserTask userTask) {
        TaskAssigneeDispatcher taskAssigneeDispatcher = context.getProcessEngineConfiguration()
            .getTaskAssigneeDispatcher();

        if (null == taskAssigneeDispatcher) {
            throw new EngineException("The taskAssigneeService can't be null for UserTask feature");
        }

        return taskAssigneeDispatcher.getTaskAssigneeCandidateInstance(userTask, context.getRequest());
    }


    /**
     * 查找低优先级的审批人
     * @param taskAssigneeCandidateInstanceList
     * @return
     */
    static List<TaskAssigneeCandidateInstance> findBatchOfHighestPriorityTaskAssigneeList(List<TaskAssigneeCandidateInstance> taskAssigneeCandidateInstanceList) {
        if(CollectionUtil.isEmpty(taskAssigneeCandidateInstanceList)) {
            return new ArrayList<TaskAssigneeCandidateInstance>(0);
        }
        int taskAssigneeCandidateInstanceSize = taskAssigneeCandidateInstanceList.size();
        //排序，升序
        if(CollectionUtil.isNotEmpty(taskAssigneeCandidateInstanceList)) {
            Collections.sort(taskAssigneeCandidateInstanceList, new Comparator<TaskAssigneeCandidateInstance>() {
                @Override
                public int compare(TaskAssigneeCandidateInstance one, TaskAssigneeCandidateInstance two) {
                    return  two.getPriority() - one.getPriority() ;
                }
            });
        }
        //优先级低的和所有相同的
        List<TaskAssigneeCandidateInstance> newTaskAssigneeCandidateInstanceList = new ArrayList<TaskAssigneeCandidateInstance>(taskAssigneeCandidateInstanceSize);
        int minPriority = 0;
        for(int i = 0; i < taskAssigneeCandidateInstanceSize; i++) {
            TaskAssigneeCandidateInstance instance = taskAssigneeCandidateInstanceList.get(i);
            if(i == 0) {
                minPriority = instance.getPriority();
                newTaskAssigneeCandidateInstanceList.add(instance);
            }else {
                if(instance.getPriority() == minPriority) {
                    newTaskAssigneeCandidateInstanceList.add(instance);
                }else {
                    break;
                }
            }
        }
        return newTaskAssigneeCandidateInstanceList;
    }

    static void buildTaskAssigneeInstance(TaskAssigneeCandidateInstance taskAssigneeCandidateInstance,
                                           List<TaskAssigneeInstance> taskAssigneeInstanceList,
                                           IdGenerator idGenerator) {
        TaskAssigneeInstance taskAssigneeInstance = new DefaultTaskAssigneeInstance();
        taskAssigneeInstance.setAssigneeId(taskAssigneeCandidateInstance.getAssigneeId());
        taskAssigneeInstance.setAssigneeType(taskAssigneeCandidateInstance.getAssigneeType());
        taskAssigneeInstance.setInstanceId(idGenerator.getId());
        taskAssigneeInstanceList.add(taskAssigneeInstance);
    }

    public static void markDoneEIAndCancelTI(ExecutionContext context, ExecutionInstance executionInstance,
                                List<ExecutionInstance> totalExecutionInstanceList, ExecutionInstanceStorage executionInstanceStorage, ProcessEngineConfiguration processEngineConfiguration) {
        // Complete all execution
        for (ExecutionInstance instance : totalExecutionInstanceList) {
            if (instance.isActive()) {
                MarkDoneUtil.markDoneExecutionInstance(instance, executionInstanceStorage,
                    processEngineConfiguration);
            }
        }

        // Find all task
        TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
        List<String> processInstanceIdList = new ArrayList<String>(2);
        processInstanceIdList.add(executionInstance.getProcessInstanceId());
        taskInstanceQueryParam.setProcessInstanceIdList(processInstanceIdList);
        taskInstanceQueryParam.setActivityInstanceId(executionInstance.getActivityInstanceId());

        TaskInstanceStorage taskInstanceStorage = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(
            ExtensionConstant.COMMON,TaskInstanceStorage.class);
        List<TaskInstance> allTaskInstanceList = taskInstanceStorage.findTaskList(taskInstanceQueryParam,
            processEngineConfiguration);

        // Cancel uncompleted task
        for (TaskInstance taskInstance : allTaskInstanceList) {

            //当前的taskInstance 已经在complete方法中更新过了
            if (taskInstance.getExecutionInstanceId().equals(executionInstance.getInstanceId())) {
                continue;
            }

            if (TaskInstanceConstant.COMPLETED.equals(taskInstance.getStatus())) {
                continue;
            }

            // 这里产生了db 读写访问,
            MarkDoneUtil.markDoneTaskInstance(taskInstance,TaskInstanceConstant.CANCELED,taskInstance.getStatus(),context.getRequest(),taskInstanceStorage,processEngineConfiguration);
        }
    }



    static void compensateExecutionAndTask(ExecutionContext context, UserTask userTask,
                                           ActivityInstance activityInstance, ExecutionInstance executionInstance,
                                           Map<String, TaskAssigneeCandidateInstance> taskAssigneeMap, ExecutionInstanceStorage executionInstanceStorage,
                                           ExecutionInstanceFactory executionInstanceFactory,
                                           TaskInstanceFactory taskInstanceFactory, ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
        List<String> processInstanceIdList = new ArrayList<String>(2);
        processInstanceIdList.add(executionInstance.getProcessInstanceId());
        taskInstanceQueryParam.setProcessInstanceIdList(processInstanceIdList);
        taskInstanceQueryParam.setActivityInstanceId(executionInstance.getActivityInstanceId());
        TaskInstanceStorage taskInstanceStorage = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(
            ExtensionConstant.COMMON,TaskInstanceStorage.class);
        List<TaskInstance> allTaskInstanceList = taskInstanceStorage.findTaskList(taskInstanceQueryParam,
            processEngineConfiguration);
        List<String> taskInstanceIdList = new ArrayList<String>();
        if(CollectionUtil.isNotEmpty(allTaskInstanceList)) {
            for(TaskInstance taskInstance : allTaskInstanceList) {
                taskInstanceIdList.add(taskInstance.getInstanceId());
            }
        }
        TaskAssigneeStorage taskAssigneeStorage = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(
            ExtensionConstant.COMMON, TaskAssigneeStorage.class);
        Map<String, List<TaskAssigneeInstance>> taskAssigneeInstanceMap = taskAssigneeStorage.findAssigneeOfInstanceList(taskInstanceIdList, processEngineConfiguration);
        if(taskAssigneeInstanceMap != null) {
            for(List<TaskAssigneeInstance> instanceList : taskAssigneeInstanceMap.values()) {
                if(CollectionUtil.isNotEmpty(instanceList)) {
                    for(TaskAssigneeInstance instance : instanceList) {
                        if(taskAssigneeMap.containsKey(instance.getAssigneeId())) {
                            taskAssigneeMap.remove(instance.getAssigneeId());
                        }
                    }
                }
            }
        }
        if(taskAssigneeMap.size() > 0) {
            List<TaskAssigneeCandidateInstance> newTaskAssigneeList = UserTaskBehaviorHelper.findBatchOfHighestPriorityTaskAssigneeList(new ArrayList<TaskAssigneeCandidateInstance>(taskAssigneeMap.values()));
            for (TaskAssigneeCandidateInstance taskAssigneeCandidateInstance : newTaskAssigneeList) {
                ExecutionInstance newExecutionInstance = executionInstanceFactory.create(activityInstance, context);
                executionInstanceStorage.insert(newExecutionInstance, processEngineConfiguration);

                TaskInstance taskInstance = taskInstanceFactory.create(userTask, newExecutionInstance, context);
                taskInstance.setPriority(taskAssigneeCandidateInstance.getPriority());
                taskInstanceStorage.insert(taskInstance, processEngineConfiguration);

                List<TaskAssigneeInstance> taskAssigneeInstanceList = new ArrayList<TaskAssigneeInstance>(2);
                IdGenerator idGenerator = context.getProcessEngineConfiguration().getIdGenerator();
                UserTaskBehaviorHelper.buildTaskAssigneeInstance(taskAssigneeCandidateInstance, taskAssigneeInstanceList, idGenerator);
                for(TaskAssigneeInstance taskAssigneeInstance : taskAssigneeInstanceList) {
                    taskAssigneeInstance.setProcessInstanceId(taskInstance.getProcessInstanceId());
                    taskAssigneeInstance.setTaskInstanceId(taskInstance.getInstanceId());
                    taskAssigneeStorage.insert(taskAssigneeInstance, processEngineConfiguration);
                }
            }
        }
    }

    public static void abortAndSetNeedPause(ExecutionContext context, ExecutionInstance executionInstance, SmartEngine smartEngine) {
        context.getProcessInstance().setStatus(InstanceStatus.aborted);
        smartEngine.getProcessCommandService().abort(executionInstance.getProcessInstanceId(),
            InstanceStatus.aborted.name());
        context.setNeedPause(true);
    }

}

