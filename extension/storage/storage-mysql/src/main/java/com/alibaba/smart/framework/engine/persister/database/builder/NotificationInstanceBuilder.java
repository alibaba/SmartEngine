package com.alibaba.smart.framework.engine.persister.database.builder;

import com.alibaba.smart.framework.engine.instance.impl.DefaultNotificationInstance;
import com.alibaba.smart.framework.engine.model.instance.NotificationInstance;
import com.alibaba.smart.framework.engine.persister.database.entity.NotificationInstanceEntity;

/**
 * 知会通知实例构建器
 * 
 * @author SmartEngine Team
 */
public final class NotificationInstanceBuilder {

    /**
     * 从Entity构建NotificationInstance
     */
    public static NotificationInstance buildNotificationInstanceFromEntity(NotificationInstanceEntity notificationInstanceEntity) {
        NotificationInstance notificationInstance = new DefaultNotificationInstance();
        
        notificationInstance.setInstanceId(notificationInstanceEntity.getId().toString());
        notificationInstance.setTenantId(notificationInstanceEntity.getTenantId());
        notificationInstance.setStartTime(notificationInstanceEntity.getGmtCreate());
        notificationInstance.setCompleteTime(notificationInstanceEntity.getGmtModified());
        
        if (notificationInstanceEntity.getProcessInstanceId() != null) {
            notificationInstance.setProcessInstanceId(notificationInstanceEntity.getProcessInstanceId().toString());
        }
        if (notificationInstanceEntity.getTaskInstanceId() != null) {
            notificationInstance.setTaskInstanceId(notificationInstanceEntity.getTaskInstanceId().toString());
        }
        notificationInstance.setSenderUserId(notificationInstanceEntity.getSenderUserId());
        notificationInstance.setReceiverUserId(notificationInstanceEntity.getReceiverUserId());
        notificationInstance.setNotificationType(notificationInstanceEntity.getNotificationType());
        notificationInstance.setTitle(notificationInstanceEntity.getTitle());
        notificationInstance.setContent(notificationInstanceEntity.getContent());
        notificationInstance.setReadStatus(notificationInstanceEntity.getReadStatus());
        notificationInstance.setReadTime(notificationInstanceEntity.getReadTime());
        
        return notificationInstance;
    }

    /**
     * 从NotificationInstance构建Entity
     */
    public static NotificationInstanceEntity buildEntityFromNotificationInstance(NotificationInstance notificationInstance) {
        NotificationInstanceEntity notificationInstanceEntity = new NotificationInstanceEntity();
        
        if (notificationInstance.getInstanceId() != null) {
            notificationInstanceEntity.setId(Long.valueOf(notificationInstance.getInstanceId()));
        }
        notificationInstanceEntity.setTenantId(notificationInstance.getTenantId());
        notificationInstanceEntity.setGmtCreate(notificationInstance.getStartTime());
        notificationInstanceEntity.setGmtModified(notificationInstance.getCompleteTime());
        
        if (notificationInstance.getProcessInstanceId() != null) {
            notificationInstanceEntity.setProcessInstanceId(Long.valueOf(notificationInstance.getProcessInstanceId()));
        }
        if (notificationInstance.getTaskInstanceId() != null) {
            notificationInstanceEntity.setTaskInstanceId(Long.valueOf(notificationInstance.getTaskInstanceId()));
        }
        notificationInstanceEntity.setSenderUserId(notificationInstance.getSenderUserId());
        notificationInstanceEntity.setReceiverUserId(notificationInstance.getReceiverUserId());
        notificationInstanceEntity.setNotificationType(notificationInstance.getNotificationType());
        notificationInstanceEntity.setTitle(notificationInstance.getTitle());
        notificationInstanceEntity.setContent(notificationInstance.getContent());
        notificationInstanceEntity.setReadStatus(notificationInstance.getReadStatus());
        notificationInstanceEntity.setReadTime(notificationInstance.getReadTime());
        
        return notificationInstanceEntity;
    }
}