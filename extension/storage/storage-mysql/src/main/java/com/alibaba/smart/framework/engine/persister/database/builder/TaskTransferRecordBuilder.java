package com.alibaba.smart.framework.engine.persister.database.builder;

import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskTransferRecord;
import com.alibaba.smart.framework.engine.model.instance.TaskTransferRecord;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskTransferRecordEntity;

/**
 * 任务移交记录Builder - 负责Model和Entity之间的转换
 *
 * @author SmartEngine Team
 */
public class TaskTransferRecordBuilder {

    /**
     * Entity转Model
     */
    public static TaskTransferRecord buildFromEntity(TaskTransferRecordEntity entity) {
        if (entity == null) {
            return null;
        }

        DefaultTaskTransferRecord record = new DefaultTaskTransferRecord();
        record.setInstanceId(entity.getId() != null ? entity.getId().toString() : null);
        record.setTaskInstanceId(entity.getTaskInstanceId() != null ? entity.getTaskInstanceId().toString() : null);
        record.setProcessInstanceId(entity.getProcessInstanceId() != null ? entity.getProcessInstanceId().toString() : null);
        record.setFromUserId(entity.getFromUserId());
        record.setToUserId(entity.getToUserId());
        record.setTransferReason(entity.getTransferReason());
        record.setDeadline(entity.getDeadline());
        record.setTenantId(entity.getTenantId());
        record.setStartTime(entity.getGmtCreate());

        return record;
    }

    /**
     * Model转Entity
     */
    public static TaskTransferRecordEntity buildEntityFrom(TaskTransferRecord record) {
        if (record == null) {
            return null;
        }

        TaskTransferRecordEntity entity = new TaskTransferRecordEntity();

        if (record.getInstanceId() != null) {
            entity.setId(Long.valueOf(record.getInstanceId()));
        }

        if (record.getTaskInstanceId() != null) {
            entity.setTaskInstanceId(Long.valueOf(record.getTaskInstanceId()));
        }

        if (record.getProcessInstanceId() != null) {
            entity.setProcessInstanceId(Long.valueOf(record.getProcessInstanceId()));
        }

        entity.setFromUserId(record.getFromUserId());
        entity.setToUserId(record.getToUserId());
        entity.setTransferReason(record.getTransferReason());
        entity.setDeadline(record.getDeadline());
        entity.setTenantId(record.getTenantId());
        entity.setGmtCreate(record.getStartTime());

        return entity;
    }
}
