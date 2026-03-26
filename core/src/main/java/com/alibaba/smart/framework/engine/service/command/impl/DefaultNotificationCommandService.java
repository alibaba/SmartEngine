package com.alibaba.smart.framework.engine.service.command.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.constant.NotificationConstant;
import com.alibaba.smart.framework.engine.exception.NotificationException;
import com.alibaba.smart.framework.engine.exception.ValidationException;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.impl.DefaultNotificationInstance;
import com.alibaba.smart.framework.engine.instance.storage.NotificationInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.NotificationInstance;
import com.alibaba.smart.framework.engine.service.command.NotificationCommandService;

/**
 * 知会通知命令服务默认实现
 * 
 * @author SmartEngine Team
 */
@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = NotificationCommandService.class)
public class DefaultNotificationCommandService implements NotificationCommandService, LifeCycleHook, ProcessEngineConfigurationAware {

    private ProcessEngineConfiguration processEngineConfiguration;
    private NotificationInstanceStorage notificationInstanceStorage;

    @Override
    public void start() {
        AnnotationScanner annotationScanner = this.processEngineConfiguration.getAnnotationScanner();
        this.notificationInstanceStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON, NotificationInstanceStorage.class);
    }

    @Override
    public void stop() {
        // 清理资源
    }

    @Override
    public List<NotificationInstance> sendNotification(String processInstanceId, String taskInstanceId,
                                                      String senderUserId, List<String> receiverUserIds,
                                                      String title, String content, String tenantId) {
        if (processInstanceId == null || senderUserId == null || receiverUserIds == null || receiverUserIds.isEmpty()) {
            throw new ValidationException("ProcessInstanceId, SenderUserId and ReceiverUserIds cannot be null or empty");
        }

        List<NotificationInstance> results = new ArrayList<>();
        for (String receiverUserId : receiverUserIds) {
            try {
                NotificationInstance notification = sendSingleNotification(processInstanceId, taskInstanceId,
                        senderUserId, receiverUserId, title, content, NotificationConstant.NotificationType.CC, tenantId);
                results.add(notification);
            } catch (Exception e) {
                throw new NotificationException("Failed to send notification to user: " + receiverUserId, e);
            }
        }
        return results;
    }

    @Override
    public NotificationInstance sendSingleNotification(String processInstanceId, String taskInstanceId,
                                                      String senderUserId, String receiverUserId,
                                                      String title, String content, String notificationType,
                                                      String tenantId) {
        if (processInstanceId == null || senderUserId == null || receiverUserId == null) {
            throw new ValidationException("ProcessInstanceId, SenderUserId and ReceiverUserId cannot be null");
        }
        
        if (notificationType == null || (!NotificationConstant.NotificationType.CC.equals(notificationType) 
                && !NotificationConstant.NotificationType.INFORM.equals(notificationType))) {
            throw new ValidationException("Invalid notification type: " + notificationType);
        }

        try {
            NotificationInstance notificationInstance = new DefaultNotificationInstance();
            notificationInstance.setProcessInstanceId(processInstanceId);
            notificationInstance.setTaskInstanceId(taskInstanceId);
            notificationInstance.setSenderUserId(senderUserId);
            notificationInstance.setReceiverUserId(receiverUserId);
            notificationInstance.setNotificationType(notificationType);
            notificationInstance.setTitle(title);
            notificationInstance.setContent(content);
            notificationInstance.setReadStatus(NotificationConstant.ReadStatus.UNREAD);
            notificationInstance.setTenantId(tenantId);
            
            // 设置ID生成器
            processEngineConfiguration.getIdGenerator().generate(notificationInstance);
            
            return notificationInstanceStorage.insert(notificationInstance, processEngineConfiguration);
        } catch (Exception e) {
            throw new NotificationException("Failed to send notification", e);
        }
    }

    @Override
    public void markAsRead(String notificationId, String tenantId) {
        if (notificationId == null) {
            throw new ValidationException("NotificationId cannot be null");
        }

        try {
            notificationInstanceStorage.markAsRead(notificationId, tenantId, processEngineConfiguration);
        } catch (Exception e) {
            throw new NotificationException("Failed to mark notification as read: " + notificationId, e);
        }
    }

    @Override
    public void batchMarkAsRead(List<String> notificationIds, String tenantId) {
        if (notificationIds == null || notificationIds.isEmpty()) {
            throw new ValidationException("NotificationIds cannot be null or empty");
        }

        try {
            notificationInstanceStorage.batchMarkAsRead(notificationIds, tenantId, processEngineConfiguration);
        } catch (Exception e) {
            throw new NotificationException("Failed to batch mark notifications as read", e);
        }
    }

    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
}