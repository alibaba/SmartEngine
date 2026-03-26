package com.alibaba.smart.framework.engine.persister.database.builder;

import com.alibaba.smart.framework.engine.instance.impl.DefaultRollbackRecord;
import com.alibaba.smart.framework.engine.model.instance.RollbackRecord;
import com.alibaba.smart.framework.engine.persister.database.entity.RollbackRecordEntity;

/**
 * 流程回退记录Builder - 负责Model和Entity之间的转换
 *
 * @author SmartEngine Team
 */
public class RollbackRecordBuilder {

    /**
     * Entity转Model
     */
    public static RollbackRecord buildFromEntity(RollbackRecordEntity entity) {
        if (entity == null) {
            return null;
        }

        DefaultRollbackRecord record = new DefaultRollbackRecord();
        record.setInstanceId(entity.getId() != null ? entity.getId().toString() : null);
        record.setProcessInstanceId(entity.getProcessInstanceId() != null ? entity.getProcessInstanceId().toString() : null);
        record.setTaskInstanceId(entity.getTaskInstanceId() != null ? entity.getTaskInstanceId().toString() : null);
        record.setRollbackType(entity.getRollbackType());
        record.setFromActivityId(entity.getFromActivityId());
        record.setToActivityId(entity.getToActivityId());
        record.setOperatorUserId(entity.getOperatorUserId());
        record.setRollbackReason(entity.getRollbackReason());
        record.setTenantId(entity.getTenantId());
        record.setStartTime(entity.getGmtCreate());

        return record;
    }

    /**
     * Model转Entity
     */
    public static RollbackRecordEntity buildEntityFrom(RollbackRecord record) {
        if (record == null) {
            return null;
        }

        RollbackRecordEntity entity = new RollbackRecordEntity();

        if (record.getInstanceId() != null) {
            entity.setId(Long.valueOf(record.getInstanceId()));
        }

        if (record.getProcessInstanceId() != null) {
            entity.setProcessInstanceId(Long.valueOf(record.getProcessInstanceId()));
        }

        if (record.getTaskInstanceId() != null) {
            entity.setTaskInstanceId(Long.valueOf(record.getTaskInstanceId()));
        }

        entity.setRollbackType(record.getRollbackType());
        entity.setFromActivityId(record.getFromActivityId());
        entity.setToActivityId(record.getToActivityId());
        entity.setOperatorUserId(record.getOperatorUserId());
        entity.setRollbackReason(record.getRollbackReason());
        entity.setTenantId(record.getTenantId());
        entity.setGmtCreate(record.getStartTime());

        return entity;
    }
}
