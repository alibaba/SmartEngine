package com.alibaba.smart.framework.engine.model.instance;

import java.util.Date;

/**
 * 知会通知实例接口
 * 
 * @author SmartEngine Team
 */
public interface NotificationInstance extends LifeCycleInstance {

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
     * 获取发送人用户ID
     */
    String getSenderUserId();

    /**
     * 设置发送人用户ID
     */
    void setSenderUserId(String senderUserId);

    /**
     * 获取接收人用户ID
     */
    String getReceiverUserId();

    /**
     * 设置接收人用户ID
     */
    void setReceiverUserId(String receiverUserId);

    /**
     * 获取通知类型
     */
    String getNotificationType();

    /**
     * 设置通知类型
     */
    void setNotificationType(String notificationType);

    /**
     * 获取通知标题
     */
    String getTitle();

    /**
     * 设置通知标题
     */
    void setTitle(String title);

    /**
     * 获取通知内容
     */
    String getContent();

    /**
     * 设置通知内容
     */
    void setContent(String content);

    /**
     * 获取读取状态
     */
    String getReadStatus();

    /**
     * 设置读取状态
     */
    void setReadStatus(String readStatus);

    /**
     * 获取读取时间
     */
    Date getReadTime();

    /**
     * 设置读取时间
     */
    void setReadTime(Date readTime);
}