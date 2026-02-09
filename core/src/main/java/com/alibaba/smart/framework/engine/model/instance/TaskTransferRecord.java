package com.alibaba.smart.framework.engine.model.instance;

import java.util.Date;

/**
 * 任务移交记录接口
 *
 * @author SmartEngine Team
 */
public interface TaskTransferRecord extends LifeCycleInstance {

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
     * 获取移交人用户ID
     */
    String getFromUserId();

    /**
     * 设置移交人用户ID
     */
    void setFromUserId(String fromUserId);

    /**
     * 获取接收人用户ID
     */
    String getToUserId();

    /**
     * 设置接收人用户ID
     */
    void setToUserId(String toUserId);

    /**
     * 获取移交原因
     */
    String getTransferReason();

    /**
     * 设置移交原因
     */
    void setTransferReason(String transferReason);

    /**
     * 获取截止时间
     */
    Date getDeadline();

    /**
     * 设置截止时间
     */
    void setDeadline(Date deadline);
}
