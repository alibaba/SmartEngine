package com.alibaba.smart.framework.engine.service.command;

import java.util.Map;

import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;

/**
 * 主要负责人工任务处理服务。
 *
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface TaskCommandService {

    ProcessInstance complete(String taskId, Map<String, Object> request);

    ProcessInstance complete(String taskId, String userId, Map<String, Object> request);

    ProcessInstance complete(String taskId, Map<String, Object> request, Map<String, Object> response);

    /**
     * 任务转交。
     */
    void transfer(String taskId, String fromUserId, String toUserId);

    /**
     * 创建任务实例. 这个相当于是数据订正，一般不需要使用。
     */
    TaskInstance createTask(ExecutionInstance executionInstance, String taskInstanceStatus, Map<String, Object> request);

    /**
     * 将任务实例标记完成. 这个相当于是数据订正，一般不需要使用。
     */
    void markDone(String taskId, Map<String, Object> request);

    /**
     * 删除任务的处理者. 这个相当于是数据订正，一般不需要使用。
     */
    void removeTaskAssigneeCandidate(String taskId, TaskAssigneeCandidateInstance taskAssigneeCandidateInstance);

    /**
     * 增加任务的处理者. 这个相当于是数据订正，一般不需要使用。
     */
    void addTaskAssigneeCandidate(String taskId, TaskAssigneeCandidateInstance taskAssigneeCandidateInstance);

}
