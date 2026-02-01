package com.alibaba.smart.framework.engine.persister.database.builder;

import com.alibaba.smart.framework.engine.common.util.IdConverter;
import com.alibaba.smart.framework.engine.instance.impl.DefaultSupervisionInstance;
import com.alibaba.smart.framework.engine.model.instance.SupervisionInstance;
import com.alibaba.smart.framework.engine.persister.database.entity.SupervisionInstanceEntity;

/**
 * Supervision instance builder.
 * Responsible for bidirectional conversion between SupervisionInstance and SupervisionInstanceEntity.
 *
 * @author SmartEngine Team
 */
public final class SupervisionInstanceBuilder {

    private SupervisionInstanceBuilder() {
        // Utility class, prevent instantiation
    }

    /**
     * Build SupervisionInstance from Entity.
     *
     * @param entity SupervisionInstanceEntity
     * @return SupervisionInstance, or null if entity is null
     */
    public static SupervisionInstance buildSupervisionInstanceFromEntity(SupervisionInstanceEntity entity) {
        if (entity == null) {
            return null;
        }

        SupervisionInstance instance = new DefaultSupervisionInstance();

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
        instance.setSupervisorUserId(entity.getSupervisorUserId());
        instance.setSupervisionReason(entity.getSupervisionReason());
        instance.setSupervisionType(entity.getSupervisionType());
        instance.setStatus(entity.getStatus());
        instance.setCloseTime(entity.getCloseTime());

        return instance;
    }

    /**
     * Build Entity from SupervisionInstance.
     *
     * @param instance SupervisionInstance
     * @return SupervisionInstanceEntity, or null if instance is null
     */
    public static SupervisionInstanceEntity buildEntityFromSupervisionInstance(SupervisionInstance instance) {
        if (instance == null) {
            return null;
        }

        SupervisionInstanceEntity entity = new SupervisionInstanceEntity();

        // ID fields with safe conversion
        entity.setId(IdConverter.toLong(instance.getInstanceId(), "instanceId"));
        entity.setProcessInstanceId(IdConverter.toLong(instance.getProcessInstanceId(), "processInstanceId"));
        entity.setTaskInstanceId(IdConverter.toLong(instance.getTaskInstanceId(), "taskInstanceId"));

        // Common fields
        entity.setTenantId(instance.getTenantId());
        entity.setGmtCreate(instance.getStartTime());
        entity.setGmtModified(instance.getCompleteTime());

        // Business fields
        entity.setSupervisorUserId(instance.getSupervisorUserId());
        entity.setSupervisionReason(instance.getSupervisionReason());
        entity.setSupervisionType(instance.getSupervisionType());
        entity.setStatus(instance.getStatus());
        entity.setCloseTime(instance.getCloseTime());

        return entity;
    }
}
