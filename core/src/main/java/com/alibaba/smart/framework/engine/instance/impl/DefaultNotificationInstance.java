package com.alibaba.smart.framework.engine.instance.impl;

import java.util.Date;

import com.alibaba.smart.framework.engine.model.instance.NotificationInstance;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 默认知会通知实例实现
 * 
 * @author SmartEngine Team
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DefaultNotificationInstance extends AbstractLifeCycleInstance implements NotificationInstance {

    private static final long serialVersionUID = 1L;

    private String processInstanceId;

    private String taskInstanceId;

    private String senderUserId;

    private String receiverUserId;

    private String notificationType;

    private String title;

    private String content;

    private String readStatus;

    private Date readTime;
}