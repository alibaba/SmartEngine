package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.common.util.IdConverter;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.storage.SupervisionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.SupervisionInstance;
import com.alibaba.smart.framework.engine.persister.database.builder.SupervisionInstanceBuilder;
import com.alibaba.smart.framework.engine.persister.database.dao.SupervisionInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.SupervisionInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.SupervisionQueryParam;

/**
 * Supervision instance relational database storage implementation.
 *
 * @author SmartEngine Team
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = SupervisionInstanceStorage.class)
public class RelationshipDatabaseSupervisionInstanceStorage implements SupervisionInstanceStorage {

    @Override
    public SupervisionInstance insert(SupervisionInstance supervisionInstance,
                                      ProcessEngineConfiguration processEngineConfiguration) {
        SupervisionInstanceDAO supervisionInstanceDAO = getDAO(processEngineConfiguration);

        SupervisionInstanceEntity entity = SupervisionInstanceBuilder
                .buildEntityFromSupervisionInstance(supervisionInstance);

        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());

        supervisionInstanceDAO.insert(entity);

        // Set generated ID back to instance
        supervisionInstance.setInstanceId(IdConverter.toString(entity.getId()));
        return supervisionInstance;
    }

    @Override
    public SupervisionInstance update(SupervisionInstance supervisionInstance,
                                      ProcessEngineConfiguration processEngineConfiguration) {
        SupervisionInstanceDAO supervisionInstanceDAO = getDAO(processEngineConfiguration);

        SupervisionInstanceEntity entity = SupervisionInstanceBuilder
                .buildEntityFromSupervisionInstance(supervisionInstance);

        entity.setGmtModified(DateUtil.getCurrentDate());

        supervisionInstanceDAO.update(entity);

        return supervisionInstance;
    }

    @Override
    public SupervisionInstance find(String supervisionId, String tenantId,
                                    ProcessEngineConfiguration processEngineConfiguration) {
        SupervisionInstanceDAO supervisionInstanceDAO = getDAO(processEngineConfiguration);

        Long id = IdConverter.toLong(supervisionId, "supervisionId");
        SupervisionInstanceEntity entity = supervisionInstanceDAO.findOne(id, tenantId);

        return SupervisionInstanceBuilder.buildSupervisionInstanceFromEntity(entity);
    }

    @Override
    public List<SupervisionInstance> findSupervisionList(SupervisionQueryParam param,
                                                         ProcessEngineConfiguration processEngineConfiguration) {
        SupervisionInstanceDAO supervisionInstanceDAO = getDAO(processEngineConfiguration);

        List<SupervisionInstanceEntity> entityList = supervisionInstanceDAO.findByQuery(param);

        return buildInstanceList(entityList);
    }

    @Override
    public Long countSupervision(SupervisionQueryParam param,
                                 ProcessEngineConfiguration processEngineConfiguration) {
        SupervisionInstanceDAO supervisionInstanceDAO = getDAO(processEngineConfiguration);

        Integer count = supervisionInstanceDAO.countByQuery(param);
        return count != null ? count.longValue() : 0L;
    }

    @Override
    public List<SupervisionInstance> findActiveSupervisionByTask(String taskInstanceId, String tenantId,
                                                                 ProcessEngineConfiguration processEngineConfiguration) {
        SupervisionInstanceDAO supervisionInstanceDAO = getDAO(processEngineConfiguration);

        Long taskId = IdConverter.toLong(taskInstanceId, "taskInstanceId");
        List<SupervisionInstanceEntity> entityList = supervisionInstanceDAO.findActiveByTaskId(taskId, tenantId);

        return buildInstanceList(entityList);
    }

    @Override
    public int closeSupervision(String supervisionId, String tenantId,
                                ProcessEngineConfiguration processEngineConfiguration) {
        SupervisionInstanceDAO supervisionInstanceDAO = getDAO(processEngineConfiguration);

        Long id = IdConverter.toLong(supervisionId, "supervisionId");
        return supervisionInstanceDAO.closeSupervision(id, tenantId);
    }

    @Override
    public int closeSupervisionByTask(String taskInstanceId, String tenantId,
                                      ProcessEngineConfiguration processEngineConfiguration) {
        SupervisionInstanceDAO supervisionInstanceDAO = getDAO(processEngineConfiguration);

        Long taskId = IdConverter.toLong(taskInstanceId, "taskInstanceId");
        return supervisionInstanceDAO.closeByTaskId(taskId, tenantId);
    }

    @Override
    public void remove(String supervisionId, String tenantId,
                       ProcessEngineConfiguration processEngineConfiguration) {
        SupervisionInstanceDAO supervisionInstanceDAO = getDAO(processEngineConfiguration);

        Long id = IdConverter.toLong(supervisionId, "supervisionId");
        supervisionInstanceDAO.delete(id, tenantId);
    }

    /**
     * Get DAO from configuration.
     */
    private SupervisionInstanceDAO getDAO(ProcessEngineConfiguration processEngineConfiguration) {
        return (SupervisionInstanceDAO) processEngineConfiguration
                .getInstanceAccessor().access("supervisionInstanceDAO");
    }

    /**
     * Build instance list from entity list.
     */
    private List<SupervisionInstance> buildInstanceList(List<SupervisionInstanceEntity> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }

        List<SupervisionInstance> instanceList = new ArrayList<>(entityList.size());
        for (SupervisionInstanceEntity entity : entityList) {
            SupervisionInstance instance = SupervisionInstanceBuilder.buildSupervisionInstanceFromEntity(entity);
            if (instance != null) {
                instanceList.add(instance);
            }
        }
        return instanceList;
    }
}
