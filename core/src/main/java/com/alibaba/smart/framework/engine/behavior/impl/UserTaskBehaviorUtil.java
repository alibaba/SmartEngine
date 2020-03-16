package com.alibaba.smart.framework.engine.behavior.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.alibaba.smart.framework.engine.bpmn.assembly.task.UserTask;
import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.configuration.TaskAssigneeDispatcher;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;

/**
 * Created by 高海军 帝奇 74394 on  2020-03-16 00:19.
 */
 abstract class UserTaskBehaviorUtil {

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
    static List<TaskAssigneeCandidateInstance> findLowPriorityTaskAssigneeList(List<TaskAssigneeCandidateInstance> taskAssigneeCandidateInstanceList) {
        if(CollectionUtil.isEmpty(taskAssigneeCandidateInstanceList)) {
            return new ArrayList<TaskAssigneeCandidateInstance>(0);
        }
        int taskAssigneeCandidateInstanceSize = taskAssigneeCandidateInstanceList.size();
        //排序，升序
        if(CollectionUtil.isNotEmpty(taskAssigneeCandidateInstanceList)) {
            Collections.sort(taskAssigneeCandidateInstanceList, new Comparator<TaskAssigneeCandidateInstance>() {
                @Override
                public int compare(TaskAssigneeCandidateInstance one, TaskAssigneeCandidateInstance two) {
                    return one.getPriority() - two.getPriority();
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
}

