package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultExecutionInstance;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTransitionInstance;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.TransitionInstance;
import com.alibaba.smart.framework.engine.persister.database.builder.ExecutionInstanceBuilder;
import com.alibaba.smart.framework.engine.persister.database.dao.ExecutionInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.ExecutionInstanceEntity;

@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = ExecutionInstanceStorage.class)
public class RelationshipDatabaseExecutionInstanceStorage implements ExecutionInstanceStorage {


    @Override
    public void insert(ExecutionInstance executionInstance,
                       ProcessEngineConfiguration processEngineConfiguration) {
        ExecutionInstanceDAO executionInstanceDAO = (ExecutionInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("executionInstanceDAO");

        ExecutionInstanceEntity executionInstanceEntity = ExecutionInstanceBuilder.buildExecutionInstanceEntity(executionInstance);

        executionInstanceDAO.insert(executionInstanceEntity);

        Long entityId = executionInstanceEntity.getId();

        // 当数据库表id 是非自增时，需要以传入的 id 值为准
        if (0L == entityId) {
            entityId = Long.valueOf(executionInstance.getInstanceId());
        }


        executionInstanceEntity = executionInstanceDAO.findOne(entityId,executionInstance.getTenantId());

        executionInstance = ExecutionInstanceBuilder.buildExecutionInstance(executionInstance, executionInstanceEntity);

    }

    @Override
    public void update(ExecutionInstance executionInstance,
                       ProcessEngineConfiguration processEngineConfiguration) {

        ExecutionInstanceDAO executionInstanceDAO = (ExecutionInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("executionInstanceDAO");
        ExecutionInstanceEntity executionInstanceEntity = ExecutionInstanceBuilder.buildExecutionInstanceEntity(executionInstance);
        executionInstanceEntity.setId(Long.valueOf(executionInstance.getInstanceId()));
        executionInstanceEntity.setGmtCreate(executionInstance.getStartTime());
        executionInstanceEntity.setGmtModified(executionInstance.getCompleteTime());

        executionInstanceDAO.update(executionInstanceEntity);
    }


    @Override
    public ExecutionInstance find(String instanceId,String tenantId,
                                  ProcessEngineConfiguration processEngineConfiguration) {

        ExecutionInstanceDAO executionInstanceDAO = (ExecutionInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("executionInstanceDAO");
        ExecutionInstanceEntity executionInstanceEntity = executionInstanceDAO.findOne(Long.valueOf(instanceId),tenantId);
        if (executionInstanceEntity == null) {
            return null;
        }
        ExecutionInstance executionInstance = new DefaultExecutionInstance();
        ExecutionInstanceBuilder.buildExecutionInstance(executionInstance, executionInstanceEntity);
        return executionInstance;

    }

    @Override
    public ExecutionInstance findWithShading(String processInstanceId, String executionInstanceId,String tenantId,
                                             ProcessEngineConfiguration processEngineConfiguration) {
        ExecutionInstanceDAO executionInstanceDAO = (ExecutionInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("executionInstanceDAO");
        ExecutionInstanceEntity executionInstanceEntity = executionInstanceDAO.findWithShading(Long.valueOf(executionInstanceId), Long.valueOf(processInstanceId),tenantId);
        ExecutionInstance executionInstance = new DefaultExecutionInstance();
        ExecutionInstanceBuilder.buildExecutionInstance(executionInstance, executionInstanceEntity);
        return executionInstance;
    }

    @Override
    public void remove(String instanceId,String tenantId,
                       ProcessEngineConfiguration processEngineConfiguration) {
        ExecutionInstanceDAO executionInstanceDAO = (ExecutionInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("executionInstanceDAO");
        executionInstanceDAO.delete(Long.valueOf(instanceId),tenantId);
    }

    @Override
    public List<ExecutionInstance> findActiveExecution(String processInstanceId,String tenantId,
                                                       ProcessEngineConfiguration processEngineConfiguration) {
        ExecutionInstanceDAO executionInstanceDAO = (ExecutionInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("executionInstanceDAO");
        List<ExecutionInstanceEntity> executionInstanceEntities = executionInstanceDAO.findActiveExecution(Long.valueOf(processInstanceId),tenantId);

        List<ExecutionInstance> executionInstanceList = Collections.emptyList();
        if (null != executionInstanceEntities) {
            executionInstanceList = new ArrayList<ExecutionInstance>(executionInstanceEntities.size());
            for (ExecutionInstanceEntity executionInstanceEntity : executionInstanceEntities) {
                ExecutionInstance executionInstance = new DefaultExecutionInstance();
                ExecutionInstanceBuilder.buildExecutionInstance(executionInstance, executionInstanceEntity);
                executionInstanceList.add(executionInstance);
            }
        }

        return executionInstanceList;
    }

    @Override
    public List<ExecutionInstance> findByActivityInstanceId(String processInstanceId, String activityInstanceId,String tenantId,
                                                            ProcessEngineConfiguration processEngineConfiguration) {
        ExecutionInstanceDAO executionInstanceDAO = (ExecutionInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("executionInstanceDAO");
        List<ExecutionInstanceEntity> executionInstanceEntities = executionInstanceDAO.findByActivityInstanceId(Long.valueOf(processInstanceId), Long.valueOf(activityInstanceId),tenantId);
        return buildList(executionInstanceEntities);
    }

    @Override
    public List<ExecutionInstance> findAll(String processInstanceId,String tenantId, ProcessEngineConfiguration processEngineConfiguration) {
        ExecutionInstanceDAO executionInstanceDAO = (ExecutionInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("executionInstanceDAO");
        List<ExecutionInstanceEntity> executionInstanceEntities = executionInstanceDAO.findAllExecutionList(Long.valueOf(processInstanceId),tenantId);
        return buildList(executionInstanceEntities);
    }

    private List<ExecutionInstance> buildList(List<ExecutionInstanceEntity> executionInstanceEntities){
        if (null != executionInstanceEntities) {
            List<ExecutionInstance> executionInstanceList = new ArrayList<ExecutionInstance>(executionInstanceEntities.size());
            for (ExecutionInstanceEntity executionInstanceEntity : executionInstanceEntities) {
                ExecutionInstance executionInstance = new DefaultExecutionInstance();
                ExecutionInstanceBuilder.buildExecutionInstance(executionInstance, executionInstanceEntity);
                executionInstanceList.add(executionInstance);
            }
            return executionInstanceList;
        }
        return new ArrayList<ExecutionInstance>();
    }
}
