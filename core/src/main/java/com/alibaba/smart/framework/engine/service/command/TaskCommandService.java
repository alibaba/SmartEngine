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

    /**
     * Claim a task by setting the claimUserId. Only unclaimed tasks can be claimed.
     */
    void claim(String taskId, String userId, String tenantId);

    ProcessInstance complete(String taskId, String userId, Map<String, Object> request);

    ProcessInstance complete(String taskId, Map<String, Object> request, Map<String, Object> response);

    /**
     * 任务转交。
     */
    void transfer(String taskId, String fromUserId, String toUserId);
    void transfer(String taskId, String fromUserId, String toUserId,String tenantId);

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
    void removeTaskAssigneeCandidate(String taskId,String tenantId, TaskAssigneeCandidateInstance taskAssigneeCandidateInstance);

    /**
     * 增加任务的处理者. 这个相当于是数据订正，一般不需要使用。
     */
    void addTaskAssigneeCandidate(String taskId,String tenantId, TaskAssigneeCandidateInstance taskAssigneeCandidateInstance);

    /**
     * 增强的任务移交，支持原因和时限
     */
    void transferWithReason(String taskId, String fromUserId, String toUserId, String reason, String tenantId);

    /**
     * 任务回退到指定节点
     */
    ProcessInstance rollbackTask(String taskId, String targetActivityId, String reason, String tenantId);

    /**
     * 增强的加签操作，支持操作记录
     */
    void addTaskAssigneeCandidateWithReason(String taskId, String tenantId, TaskAssigneeCandidateInstance taskAssigneeCandidateInstance, String reason);

    /**
     * 增强的减签操作，支持操作记录
     */
    void removeTaskAssigneeCandidateWithReason(String taskId, String tenantId, TaskAssigneeCandidateInstance taskAssigneeCandidateInstance, String reason);

}
