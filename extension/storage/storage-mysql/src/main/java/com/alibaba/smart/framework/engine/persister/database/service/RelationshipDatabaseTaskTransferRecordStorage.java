package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.storage.TaskTransferRecordStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskTransferRecord;
import com.alibaba.smart.framework.engine.persister.database.builder.TaskTransferRecordBuilder;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskTransferRecordDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskTransferRecordEntity;

/**
 * 任务移交记录关系数据库存储实现
 *
 * @author SmartEngine Team
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = TaskTransferRecordStorage.class)
public class RelationshipDatabaseTaskTransferRecordStorage implements TaskTransferRecordStorage {

    @Override
    public TaskTransferRecord insert(TaskTransferRecord taskTransferRecord,
                                     ProcessEngineConfiguration processEngineConfiguration) {
        TaskTransferRecordDAO taskTransferRecordDAO = (TaskTransferRecordDAO) processEngineConfiguration
                .getInstanceAccessor().access("taskTransferRecordDAO");

        TaskTransferRecordEntity entity = TaskTransferRecordBuilder.buildEntityFrom(taskTransferRecord);

        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());

        taskTransferRecordDAO.insert(entity);

        taskTransferRecord.setInstanceId(entity.getId().toString());
        return taskTransferRecord;
    }

    @Override
    public List<TaskTransferRecord> findByTaskId(Long taskInstanceId, String tenantId,
                                                 ProcessEngineConfiguration processEngineConfiguration) {
        TaskTransferRecordDAO taskTransferRecordDAO = (TaskTransferRecordDAO) processEngineConfiguration
                .getInstanceAccessor().access("taskTransferRecordDAO");

        List<TaskTransferRecordEntity> entityList = taskTransferRecordDAO
                .selectByTaskInstanceId(taskInstanceId, tenantId);

        List<TaskTransferRecord> recordList = new ArrayList<>(entityList.size());
        for (TaskTransferRecordEntity entity : entityList) {
            TaskTransferRecord record = TaskTransferRecordBuilder.buildFromEntity(entity);
            recordList.add(record);
        }

        return recordList;
    }

    @Override
    public TaskTransferRecord find(Long id, String tenantId,
                                   ProcessEngineConfiguration processEngineConfiguration) {
        TaskTransferRecordDAO taskTransferRecordDAO = (TaskTransferRecordDAO) processEngineConfiguration
                .getInstanceAccessor().access("taskTransferRecordDAO");

        TaskTransferRecordEntity entity = taskTransferRecordDAO.select(id, tenantId);

        if (entity == null) {
            return null;
        }

        return TaskTransferRecordBuilder.buildFromEntity(entity);
    }

    @Override
    public TaskTransferRecord update(TaskTransferRecord taskTransferRecord,
                                     ProcessEngineConfiguration processEngineConfiguration) {
        TaskTransferRecordDAO taskTransferRecordDAO = (TaskTransferRecordDAO) processEngineConfiguration
                .getInstanceAccessor().access("taskTransferRecordDAO");

        TaskTransferRecordEntity entity = TaskTransferRecordBuilder.buildEntityFrom(taskTransferRecord);

        entity.setGmtModified(DateUtil.getCurrentDate());

        taskTransferRecordDAO.update(entity);

        return taskTransferRecord;
    }

    @Override
    public void remove(Long id, String tenantId,
                      ProcessEngineConfiguration processEngineConfiguration) {
        TaskTransferRecordDAO taskTransferRecordDAO = (TaskTransferRecordDAO) processEngineConfiguration
                .getInstanceAccessor().access("taskTransferRecordDAO");

        taskTransferRecordDAO.delete(id, tenantId);
    }
}
