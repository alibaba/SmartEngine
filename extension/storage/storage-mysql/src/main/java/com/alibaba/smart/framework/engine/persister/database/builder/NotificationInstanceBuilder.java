package com.alibaba.smart.framework.engine.persister.database.builder;

import com.alibaba.smart.framework.engine.common.util.IdConverter;
import com.alibaba.smart.framework.engine.instance.impl.DefaultNotificationInstance;
import com.alibaba.smart.framework.engine.model.instance.NotificationInstance;
import com.alibaba.smart.framework.engine.persister.database.entity.NotificationInstanceEntity;

/**
 * Notification instance builder.
 * Responsible for bidirectional conversion between NotificationInstance and NotificationInstanceEntity.
 *
 * @author SmartEngine Team
 */
public final class NotificationInstanceBuilder {

    private NotificationInstanceBuilder() {
        // Utility class, prevent instantiation
    }

    /**
     * Build NotificationInstance from Entity.
     *
     * @param entity NotificationInstanceEntity
     * @return NotificationInstance, or null if entity is null
     */
    public static NotificationInstance buildNotificationInstanceFromEntity(NotificationInstanceEntity entity) {
        if (entity == null) {
            return null;
        }

        NotificationInstance instance = new DefaultNotificationInstance();

        // ID fields with null check
        if (entity.getId() != null) {
            instance.setInstanceId(IdConverter.toString(entity.getId()));
        }
        if (entity.getProcessInstanceId() != null) {
            instance.setProcessInstanceId(IdConverter.toString(entity.getProcessInstanceId()));
        }
        if (entity.getTaskInstanceId() != null) {
            instance.setTaskInstanceId(IdConverter.toString(entity.getTaskInstanceId()));
        }

        // Common fields
        instance.setTenantId(entity.getTenantId());
        instance.setStartTime(entity.getGmtCreate());
        instance.setCompleteTime(entity.getGmtModified());

        // Business fields
        instance.setSenderUserId(entity.getSenderUserId());
        instance.setReceiverUserId(entity.getReceiverUserId());
        instance.setNotificationType(entity.getNotificationType());
        instance.setTitle(entity.getTitle());
        instance.setContent(entity.getContent());
        instance.setReadStatus(entity.getReadStatus());
        instance.setReadTime(entity.getReadTime());

        return instance;
    }

    /**
     * Build Entity from NotificationInstance.
     *
     * @param instance NotificationInstance
     * @return NotificationInstanceEntity, or null if instance is null
     */
    public static NotificationInstanceEntity buildEntityFromNotificationInstance(NotificationInstance instance) {
        if (instance == null) {
            return null;
        }

        NotificationInstanceEntity entity = new NotificationInstanceEntity();

        // ID fields with safe conversion
        entity.setId(IdConverter.toLong(instance.getInstanceId(), "instanceId"));
        entity.setProcessInstanceId(IdConverter.toLong(instance.getProcessInstanceId(), "processInstanceId"));
        entity.setTaskInstanceId(IdConverter.toLong(instance.getTaskInstanceId(), "taskInstanceId"));

        // Common fields
        entity.setTenantId(instance.getTenantId());
        entity.setGmtCreate(instance.getStartTime());
        entity.setGmtModified(instance.getCompleteTime());

        // Business fields
        entity.setSenderUserId(instance.getSenderUserId());
        entity.setReceiverUserId(instance.getReceiverUserId());
        entity.setNotificationType(instance.getNotificationType());
        entity.setTitle(instance.getTitle());
        entity.setContent(instance.getContent());
        entity.setReadStatus(instance.getReadStatus());
        entity.setReadTime(instance.getReadTime());

        return entity;
    }
}
