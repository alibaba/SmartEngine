package com.alibaba.smart.framework.engine.persister.database.builder;

import com.alibaba.smart.framework.engine.instance.impl.DefaultSupervisionInstance;
import com.alibaba.smart.framework.engine.model.instance.SupervisionInstance;
import com.alibaba.smart.framework.engine.persister.database.entity.SupervisionInstanceEntity;

/**
 * 督办实例构建器
 * 
 * @author SmartEngine Team
 */
public final class SupervisionInstanceBuilder {

    /**
     * 从Entity构建SupervisionInstance
     */
    public static SupervisionInstance buildSupervisionInstanceFromEntity(SupervisionInstanceEntity supervisionInstanceEntity) {
        SupervisionInstance supervisionInstance = new DefaultSupervisionInstance();
        
        supervisionInstance.setInstanceId(supervisionInstanceEntity.getId().toString());
        supervisionInstance.setTenantId(supervisionInstanceEntity.getTenantId());
        supervisionInstance.setStartTime(supervisionInstanceEntity.getGmtCreate());
        supervisionInstance.setCompleteTime(supervisionInstanceEntity.getGmtModified());
        
        supervisionInstance.setProcessInstanceId(supervisionInstanceEntity.getProcessInstanceId().toString());
        supervisionInstance.setTaskInstanceId(supervisionInstanceEntity.getTaskInstanceId().toString());
        supervisionInstance.setSupervisorUserId(supervisionInstanceEntity.getSupervisorUserId());
        supervisionInstance.setSupervisionReason(supervisionInstanceEntity.getSupervisionReason());
        supervisionInstance.setSupervisionType(supervisionInstanceEntity.getSupervisionType());
        supervisionInstance.setStatus(supervisionInstanceEntity.getStatus());
        supervisionInstance.setCloseTime(supervisionInstanceEntity.getCloseTime());
        
        return supervisionInstance;
    }

    /**
     * 从SupervisionInstance构建Entity
     */
    public static SupervisionInstanceEntity buildEntityFromSupervisionInstance(SupervisionInstance supervisionInstance) {
        SupervisionInstanceEntity supervisionInstanceEntity = new SupervisionInstanceEntity();
        
        if (supervisionInstance.getInstanceId() != null) {
            supervisionInstanceEntity.setId(Long.valueOf(supervisionInstance.getInstanceId()));
        }
        supervisionInstanceEntity.setTenantId(supervisionInstance.getTenantId());
        supervisionInstanceEntity.setGmtCreate(supervisionInstance.getStartTime());
        supervisionInstanceEntity.setGmtModified(supervisionInstance.getCompleteTime());
        
        if (supervisionInstance.getProcessInstanceId() != null) {
            supervisionInstanceEntity.setProcessInstanceId(Long.valueOf(supervisionInstance.getProcessInstanceId()));
        }
        if (supervisionInstance.getTaskInstanceId() != null) {
            supervisionInstanceEntity.setTaskInstanceId(Long.valueOf(supervisionInstance.getTaskInstanceId()));
        }
        supervisionInstanceEntity.setSupervisorUserId(supervisionInstance.getSupervisorUserId());
        supervisionInstanceEntity.setSupervisionReason(supervisionInstance.getSupervisionReason());
        supervisionInstanceEntity.setSupervisionType(supervisionInstance.getSupervisionType());
        supervisionInstanceEntity.setStatus(supervisionInstance.getStatus());
        supervisionInstanceEntity.setCloseTime(supervisionInstance.getCloseTime());
        
        return supervisionInstanceEntity;
    }
}