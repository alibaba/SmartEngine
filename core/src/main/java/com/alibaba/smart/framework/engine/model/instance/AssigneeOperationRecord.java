package com.alibaba.smart.framework.engine.model.instance;

/**
 * 加签减签操作记录接口
 *
 * @author SmartEngine Team
 */
public interface AssigneeOperationRecord extends LifeCycleInstance {

    /**
     * 获取流程实例ID
     */
    String getProcessInstanceId();

    /**
     * 设置流程实例ID
     */
    void setProcessInstanceId(String processInstanceId);

    /**
     * 获取任务实例ID
     */
    String getTaskInstanceId();

    /**
     * 设置任务实例ID
     */
    void setTaskInstanceId(String taskInstanceId);

    /**
     * 获取操作类型（add_assignee/remove_assignee）
     */
    String getOperationType();

    /**
     * 设置操作类型
     */
    void setOperationType(String operationType);

    /**
     * 获取操作人用户ID
     */
    String getOperatorUserId();

    /**
     * 设置操作人用户ID
     */
    void setOperatorUserId(String operatorUserId);

    /**
     * 获取目标用户ID
     */
    String getTargetUserId();

    /**
     * 设置目标用户ID
     */
    void setTargetUserId(String targetUserId);

    /**
     * 获取操作原因
     */
    String getOperationReason();

    /**
     * 设置操作原因
     */
    void setOperationReason(String operationReason);
}
