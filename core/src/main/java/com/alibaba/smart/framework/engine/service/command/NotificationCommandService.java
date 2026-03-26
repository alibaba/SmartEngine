package com.alibaba.smart.framework.engine.service.command;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.NotificationInstance;

/**
 * 知会抄送命令服务接口
 * 
 * @author SmartEngine Team
 */
public interface NotificationCommandService {

    /**
     * 发送知会通知
     * 
     * @param processInstanceId 流程实例ID
     * @param taskInstanceId 任务实例ID（可选）
     * @param senderUserId 发送人用户ID
     * @param receiverUserIds 接收人用户ID列表
     * @param title 通知标题
     * @param content 通知内容
     * @param tenantId 租户ID
     * @return 知会通知实例列表
     */
    List<NotificationInstance> sendNotification(String processInstanceId, String taskInstanceId,
                                               String senderUserId, List<String> receiverUserIds,
                                               String title, String content, String tenantId);

    /**
     * 发送单个知会通知
     * 
     * @param processInstanceId 流程实例ID
     * @param taskInstanceId 任务实例ID（可选）
     * @param senderUserId 发送人用户ID
     * @param receiverUserId 接收人用户ID
     * @param title 通知标题
     * @param content 通知内容
     * @param notificationType 通知类型
     * @param tenantId 租户ID
     * @return 知会通知实例
     */
    NotificationInstance sendSingleNotification(String processInstanceId, String taskInstanceId,
                                               String senderUserId, String receiverUserId,
                                               String title, String content, String notificationType,
                                               String tenantId);

    /**
     * 标记为已读
     * 
     * @param notificationId 通知ID
     * @param tenantId 租户ID
     */
    void markAsRead(String notificationId, String tenantId);

    /**
     * 批量标记为已读
     * 
     * @param notificationIds 通知ID列表
     * @param tenantId 租户ID
     */
    void batchMarkAsRead(List<String> notificationIds, String tenantId);
}