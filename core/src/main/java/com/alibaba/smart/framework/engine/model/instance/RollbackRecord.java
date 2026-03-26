package com.alibaba.smart.framework.engine.model.instance;

/**
 * 流程回退记录接口
 *
 * @author SmartEngine Team
 */
public interface RollbackRecord extends LifeCycleInstance {

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
     * 获取回退类型（specific/previous）
     */
    String getRollbackType();

    /**
     * 设置回退类型
     */
    void setRollbackType(String rollbackType);

    /**
     * 获取源活动ID
     */
    String getFromActivityId();

    /**
     * 设置源活动ID
     */
    void setFromActivityId(String fromActivityId);

    /**
     * 获取目标活动ID
     */
    String getToActivityId();

    /**
     * 设置目标活动ID
     */
    void setToActivityId(String toActivityId);

    /**
     * 获取操作人用户ID
     */
    String getOperatorUserId();

    /**
     * 设置操作人用户ID
     */
    void setOperatorUserId(String operatorUserId);

    /**
     * 获取回退原因
     */
    String getRollbackReason();

    /**
     * 设置回退原因
     */
    void setRollbackReason(String rollbackReason);
}
