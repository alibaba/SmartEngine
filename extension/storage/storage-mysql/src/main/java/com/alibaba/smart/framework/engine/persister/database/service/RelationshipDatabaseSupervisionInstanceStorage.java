package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.SupervisionConstant;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultSupervisionInstance;
import com.alibaba.smart.framework.engine.instance.storage.SupervisionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.SupervisionInstance;
import com.alibaba.smart.framework.engine.persister.database.builder.SupervisionInstanceBuilder;
import com.alibaba.smart.framework.engine.persister.database.dao.SupervisionInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.SupervisionInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.SupervisionQueryParam;

/**
 * 督办实例关系数据库存储实现
 * 
 * @author SmartEngine Team
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = SupervisionInstanceStorage.class)
public class RelationshipDatabaseSupervisionInstanceStorage implements SupervisionInstanceStorage {

    @Override
    public SupervisionInstance insert(SupervisionInstance supervisionInstance,
                                     ProcessEngineConfiguration processEngineConfiguration) {
        SupervisionInstanceDAO supervisionInstanceDAO = (SupervisionInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("supervisionInstanceDAO");
        
        SupervisionInstanceEntity supervisionInstanceEntity = SupervisionInstanceBuilder
                .buildEntityFromSupervisionInstance(supervisionInstance);
        
        supervisionInstanceEntity.setGmtCreate(DateUtil.getCurrentDate());
        supervisionInstanceEntity.setGmtModified(DateUtil.getCurrentDate());
        
        supervisionInstanceDAO.insert(supervisionInstanceEntity);
        
        supervisionInstance.setInstanceId(supervisionInstanceEntity.getId().toString());
        return supervisionInstance;
    }

    @Override
    public SupervisionInstance update(SupervisionInstance supervisionInstance,
                                     ProcessEngineConfiguration processEngineConfiguration) {
        SupervisionInstanceDAO supervisionInstanceDAO = (SupervisionInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("supervisionInstanceDAO");
        
        SupervisionInstanceEntity supervisionInstanceEntity = SupervisionInstanceBuilder
                .buildEntityFromSupervisionInstance(supervisionInstance);
        
        supervisionInstanceEntity.setGmtModified(DateUtil.getCurrentDate());
        
        supervisionInstanceDAO.update(supervisionInstanceEntity);
        
        return supervisionInstance;
    }

    @Override
    public SupervisionInstance find(String supervisionId, String tenantId,
                                   ProcessEngineConfiguration processEngineConfiguration) {
        SupervisionInstanceDAO supervisionInstanceDAO = (SupervisionInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("supervisionInstanceDAO");
        
        SupervisionInstanceEntity supervisionInstanceEntity = supervisionInstanceDAO
                .findOne(Long.valueOf(supervisionId), tenantId);
        
        if (supervisionInstanceEntity == null) {
            return null;
        }
        
        return SupervisionInstanceBuilder.buildSupervisionInstanceFromEntity(supervisionInstanceEntity);
    }

    @Override
    public List<SupervisionInstance> findSupervisionList(SupervisionQueryParam param,
                                                         ProcessEngineConfiguration processEngineConfiguration) {
        SupervisionInstanceDAO supervisionInstanceDAO = (SupervisionInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("supervisionInstanceDAO");
        
        // 使用综合查询方法
        List<SupervisionInstanceEntity> supervisionInstanceEntityList = supervisionInstanceDAO
                .findByQuery(param);
        
        List<SupervisionInstance> supervisionInstanceList = new ArrayList<>(supervisionInstanceEntityList.size());
        for (SupervisionInstanceEntity supervisionInstanceEntity : supervisionInstanceEntityList) {
            SupervisionInstance supervisionInstance = SupervisionInstanceBuilder
                    .buildSupervisionInstanceFromEntity(supervisionInstanceEntity);
            supervisionInstanceList.add(supervisionInstance);
        }
        
        return supervisionInstanceList;
    }

    @Override
    public Long countSupervision(SupervisionQueryParam param,
                                ProcessEngineConfiguration processEngineConfiguration) {
        SupervisionInstanceDAO supervisionInstanceDAO = (SupervisionInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("supervisionInstanceDAO");

        Integer count = supervisionInstanceDAO.countByQuery(param);
        return count != null ? count.longValue() : 0L;
    }

    @Override
    public List<SupervisionInstance> findActiveSupervisionByTask(String taskInstanceId, String tenantId,
                                                                ProcessEngineConfiguration processEngineConfiguration) {
        SupervisionInstanceDAO supervisionInstanceDAO = (SupervisionInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("supervisionInstanceDAO");
        
        List<SupervisionInstanceEntity> supervisionInstanceEntityList = supervisionInstanceDAO
                .findActiveByTaskId(Long.valueOf(taskInstanceId), tenantId);
        
        List<SupervisionInstance> supervisionInstanceList = new ArrayList<>(supervisionInstanceEntityList.size());
        for (SupervisionInstanceEntity supervisionInstanceEntity : supervisionInstanceEntityList) {
            SupervisionInstance supervisionInstance = SupervisionInstanceBuilder
                    .buildSupervisionInstanceFromEntity(supervisionInstanceEntity);
            supervisionInstanceList.add(supervisionInstance);
        }
        
        return supervisionInstanceList;
    }

    @Override
    public int closeSupervision(String supervisionId, String tenantId,
                               ProcessEngineConfiguration processEngineConfiguration) {
        SupervisionInstanceDAO supervisionInstanceDAO = (SupervisionInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("supervisionInstanceDAO");
        
        return supervisionInstanceDAO.closeSupervision(Long.valueOf(supervisionId), tenantId);
    }

    @Override
    public int closeSupervisionByTask(String taskInstanceId, String tenantId,
                                     ProcessEngineConfiguration processEngineConfiguration) {
        SupervisionInstanceDAO supervisionInstanceDAO = (SupervisionInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("supervisionInstanceDAO");
        
        return supervisionInstanceDAO.closeByTaskId(Long.valueOf(taskInstanceId), tenantId);
    }

    @Override
    public void remove(String supervisionId, String tenantId,
                      ProcessEngineConfiguration processEngineConfiguration) {
        SupervisionInstanceDAO supervisionInstanceDAO = (SupervisionInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("supervisionInstanceDAO");
        
        supervisionInstanceDAO.delete(Long.valueOf(supervisionId), tenantId);
    }
}