package com.alibaba.smart.framework.engine.persister.database.builder;

import com.alibaba.smart.framework.engine.instance.impl.DefaultAssigneeOperationRecord;
import com.alibaba.smart.framework.engine.model.instance.AssigneeOperationRecord;
import com.alibaba.smart.framework.engine.persister.database.entity.AssigneeOperationRecordEntity;

/**
 * 加签减签操作记录Builder - 负责Model和Entity之间的转换
 *
 * @author SmartEngine Team
 */
public class AssigneeOperationRecordBuilder {

    /**
     * Entity转Model
     */
    public static AssigneeOperationRecord buildFromEntity(AssigneeOperationRecordEntity entity) {
        if (entity == null) {
            return null;
        }

        DefaultAssigneeOperationRecord record = new DefaultAssigneeOperationRecord();
        record.setInstanceId(entity.getId() != null ? entity.getId().toString() : null);
        record.setTaskInstanceId(entity.getTaskInstanceId() != null ? entity.getTaskInstanceId().toString() : null);
        record.setProcessInstanceId(entity.getProcessInstanceId() != null ? entity.getProcessInstanceId().toString() : null);
        record.setOperationType(entity.getOperationType());
        record.setOperatorUserId(entity.getOperatorUserId());
        record.setTargetUserId(entity.getTargetUserId());
        record.setOperationReason(entity.getOperationReason());
        record.setTenantId(entity.getTenantId());
        record.setStartTime(entity.getGmtCreate());

        return record;
    }

    /**
     * Model转Entity
     */
    public static AssigneeOperationRecordEntity buildEntityFrom(AssigneeOperationRecord record) {
        if (record == null) {
            return null;
        }

        AssigneeOperationRecordEntity entity = new AssigneeOperationRecordEntity();

        if (record.getInstanceId() != null) {
            entity.setId(Long.valueOf(record.getInstanceId()));
        }

        if (record.getTaskInstanceId() != null) {
            entity.setTaskInstanceId(Long.valueOf(record.getTaskInstanceId()));
        }

        if (record.getProcessInstanceId() != null) {
            entity.setProcessInstanceId(Long.valueOf(record.getProcessInstanceId()));
        }

        entity.setOperationType(record.getOperationType());
        entity.setOperatorUserId(record.getOperatorUserId());
        entity.setTargetUserId(record.getTargetUserId());
        entity.setOperationReason(record.getOperationReason());
        entity.setTenantId(record.getTenantId());
        entity.setGmtCreate(record.getStartTime());

        return entity;
    }
}
