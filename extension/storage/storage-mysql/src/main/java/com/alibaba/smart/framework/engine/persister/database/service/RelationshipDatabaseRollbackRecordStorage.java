package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.storage.RollbackRecordStorage;
import com.alibaba.smart.framework.engine.model.instance.RollbackRecord;
import com.alibaba.smart.framework.engine.persister.database.builder.RollbackRecordBuilder;
import com.alibaba.smart.framework.engine.persister.database.dao.RollbackRecordDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.RollbackRecordEntity;

/**
 * 流程回退记录关系数据库存储实现
 *
 * @author SmartEngine Team
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = RollbackRecordStorage.class)
public class RelationshipDatabaseRollbackRecordStorage implements RollbackRecordStorage {

    @Override
    public RollbackRecord insert(RollbackRecord rollbackRecord,
                                ProcessEngineConfiguration processEngineConfiguration) {
        RollbackRecordDAO rollbackRecordDAO = (RollbackRecordDAO) processEngineConfiguration
                .getInstanceAccessor().access("rollbackRecordDAO");

        RollbackRecordEntity entity = RollbackRecordBuilder.buildEntityFrom(rollbackRecord);

        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());

        rollbackRecordDAO.insert(entity);

        rollbackRecord.setInstanceId(entity.getId().toString());
        return rollbackRecord;
    }

    @Override
    public List<RollbackRecord> findByProcessInstanceId(Long processInstanceId, String tenantId,
                                                       ProcessEngineConfiguration processEngineConfiguration) {
        RollbackRecordDAO rollbackRecordDAO = (RollbackRecordDAO) processEngineConfiguration
                .getInstanceAccessor().access("rollbackRecordDAO");

        List<RollbackRecordEntity> entityList = rollbackRecordDAO
                .selectByProcessInstanceId(processInstanceId, tenantId);

        List<RollbackRecord> recordList = new ArrayList<>(entityList.size());
        for (RollbackRecordEntity entity : entityList) {
            RollbackRecord record = RollbackRecordBuilder.buildFromEntity(entity);
            recordList.add(record);
        }

        return recordList;
    }

    @Override
    public RollbackRecord find(Long id, String tenantId,
                              ProcessEngineConfiguration processEngineConfiguration) {
        RollbackRecordDAO rollbackRecordDAO = (RollbackRecordDAO) processEngineConfiguration
                .getInstanceAccessor().access("rollbackRecordDAO");

        RollbackRecordEntity entity = rollbackRecordDAO.select(id, tenantId);

        if (entity == null) {
            return null;
        }

        return RollbackRecordBuilder.buildFromEntity(entity);
    }

    @Override
    public RollbackRecord update(RollbackRecord rollbackRecord,
                                ProcessEngineConfiguration processEngineConfiguration) {
        RollbackRecordDAO rollbackRecordDAO = (RollbackRecordDAO) processEngineConfiguration
                .getInstanceAccessor().access("rollbackRecordDAO");

        RollbackRecordEntity entity = RollbackRecordBuilder.buildEntityFrom(rollbackRecord);

        entity.setGmtModified(DateUtil.getCurrentDate());

        rollbackRecordDAO.update(entity);

        return rollbackRecord;
    }

    @Override
    public void remove(Long id, String tenantId,
                      ProcessEngineConfiguration processEngineConfiguration) {
        RollbackRecordDAO rollbackRecordDAO = (RollbackRecordDAO) processEngineConfiguration
                .getInstanceAccessor().access("rollbackRecordDAO");

        rollbackRecordDAO.delete(id, tenantId);
    }
}
