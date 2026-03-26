package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.storage.AssigneeOperationRecordStorage;
import com.alibaba.smart.framework.engine.model.instance.AssigneeOperationRecord;
import com.alibaba.smart.framework.engine.persister.database.builder.AssigneeOperationRecordBuilder;
import com.alibaba.smart.framework.engine.persister.database.dao.AssigneeOperationRecordDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.AssigneeOperationRecordEntity;

/**
 * 加签减签操作记录关系数据库存储实现
 *
 * @author SmartEngine Team
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = AssigneeOperationRecordStorage.class)
public class RelationshipDatabaseAssigneeOperationRecordStorage implements AssigneeOperationRecordStorage {

    @Override
    public AssigneeOperationRecord insert(AssigneeOperationRecord assigneeOperationRecord,
                                         ProcessEngineConfiguration processEngineConfiguration) {
        AssigneeOperationRecordDAO assigneeOperationRecordDAO = (AssigneeOperationRecordDAO) processEngineConfiguration
                .getInstanceAccessor().access("assigneeOperationRecordDAO");

        AssigneeOperationRecordEntity entity = AssigneeOperationRecordBuilder.buildEntityFrom(assigneeOperationRecord);

        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());

        assigneeOperationRecordDAO.insert(entity);

        assigneeOperationRecord.setInstanceId(entity.getId().toString());
        return assigneeOperationRecord;
    }

    @Override
    public List<AssigneeOperationRecord> findByTaskId(Long taskInstanceId, String tenantId,
                                                      ProcessEngineConfiguration processEngineConfiguration) {
        AssigneeOperationRecordDAO assigneeOperationRecordDAO = (AssigneeOperationRecordDAO) processEngineConfiguration
                .getInstanceAccessor().access("assigneeOperationRecordDAO");

        List<AssigneeOperationRecordEntity> entityList = assigneeOperationRecordDAO
                .selectByTaskInstanceId(taskInstanceId, tenantId);

        List<AssigneeOperationRecord> recordList = new ArrayList<>(entityList.size());
        for (AssigneeOperationRecordEntity entity : entityList) {
            AssigneeOperationRecord record = AssigneeOperationRecordBuilder.buildFromEntity(entity);
            recordList.add(record);
        }

        return recordList;
    }

    @Override
    public AssigneeOperationRecord find(Long id, String tenantId,
                                       ProcessEngineConfiguration processEngineConfiguration) {
        AssigneeOperationRecordDAO assigneeOperationRecordDAO = (AssigneeOperationRecordDAO) processEngineConfiguration
                .getInstanceAccessor().access("assigneeOperationRecordDAO");

        AssigneeOperationRecordEntity entity = assigneeOperationRecordDAO.select(id, tenantId);

        if (entity == null) {
            return null;
        }

        return AssigneeOperationRecordBuilder.buildFromEntity(entity);
    }

    @Override
    public AssigneeOperationRecord update(AssigneeOperationRecord assigneeOperationRecord,
                                         ProcessEngineConfiguration processEngineConfiguration) {
        AssigneeOperationRecordDAO assigneeOperationRecordDAO = (AssigneeOperationRecordDAO) processEngineConfiguration
                .getInstanceAccessor().access("assigneeOperationRecordDAO");

        AssigneeOperationRecordEntity entity = AssigneeOperationRecordBuilder.buildEntityFrom(assigneeOperationRecord);

        entity.setGmtModified(DateUtil.getCurrentDate());

        assigneeOperationRecordDAO.update(entity);

        return assigneeOperationRecord;
    }

    @Override
    public void remove(Long id, String tenantId,
                      ProcessEngineConfiguration processEngineConfiguration) {
        AssigneeOperationRecordDAO assigneeOperationRecordDAO = (AssigneeOperationRecordDAO) processEngineConfiguration
                .getInstanceAccessor().access("assigneeOperationRecordDAO");

        assigneeOperationRecordDAO.delete(id, tenantId);
    }
}
