package com.alibaba.smart.framework.engine.model.instance;

import java.util.Date;

/**
 * 督办实例接口
 * 
 * @author SmartEngine Team
 */
public interface SupervisionInstance extends LifeCycleInstance {

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
     * 获取督办人用户ID
     */
    String getSupervisorUserId();

    /**
     * 设置督办人用户ID
     */
    void setSupervisorUserId(String supervisorUserId);

    /**
     * 获取督办原因
     */
    String getSupervisionReason();

    /**
     * 设置督办原因
     */
    void setSupervisionReason(String supervisionReason);

    /**
     * 获取督办类型
     */
    String getSupervisionType();

    /**
     * 设置督办类型
     */
    void setSupervisionType(String supervisionType);

    /**
     * 获取督办状态
     */
    String getStatus();

    /**
     * 设置督办状态
     */
    void setStatus(String status);

    /**
     * 获取关闭时间
     */
    Date getCloseTime();

    /**
     * 设置关闭时间
     */
    void setCloseTime(Date closeTime);
}