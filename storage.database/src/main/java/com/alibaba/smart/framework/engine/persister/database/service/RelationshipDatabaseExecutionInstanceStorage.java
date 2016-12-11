package com.alibaba.smart.framework.engine.persister.database.service;

import com.alibaba.smart.framework.engine.instance.impl.DefaultExecutionInstance;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.ExecutionInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.dao.ProcessInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.ExecutionInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.ProcessInstanceEntity;
import com.alibaba.smart.framework.engine.persister.util.SpringContextUtil;

import java.util.List;


public class RelationshipDatabaseExecutionInstanceStorage implements ExecutionInstanceStorage {


    @Override
    public ExecutionInstance insert(ExecutionInstance executionInstance) {
        ExecutionInstanceDAO executionInstanceDAO= (ExecutionInstanceDAO) SpringContextUtil.getBean("executionInstanceDAO");

        ExecutionInstanceEntity executionInstanceEntity = buildExecutionInstanceEntity(executionInstance);


        executionInstanceDAO.insert(executionInstanceEntity);


         executionInstance = buildExecutionInstance(executionInstance, executionInstanceEntity);
        return executionInstance;

    }

    private ExecutionInstanceEntity buildExecutionInstanceEntity(ExecutionInstance executionInstance) {
        ExecutionInstanceEntity executionInstanceEntity = new ExecutionInstanceEntity();
        executionInstanceEntity.setProcessDefinitionId(executionInstance.getProcessDefinitionIdAndVersion());
        executionInstanceEntity.setProcessInstanceId(executionInstance.getProcessInstanceId());
        executionInstanceEntity.setActivityInstanceId(executionInstance.getActivityInstanceId());
        executionInstanceEntity.setProcessDefinitionActivityId(executionInstance.getActivityId());
        return executionInstanceEntity;
    }

    @Override
    public ExecutionInstance update(ExecutionInstance executionInstance) {

        ExecutionInstanceDAO executionInstanceDAO= (ExecutionInstanceDAO) SpringContextUtil.getBean("executionInstanceDAO");
        ExecutionInstanceEntity executionInstanceEntity = buildExecutionInstanceEntity(executionInstance);

        executionInstanceDAO.update(executionInstanceEntity);
        return executionInstance;
    }

    private ExecutionInstance buildExecutionInstance(ExecutionInstance executionInstance, ExecutionInstanceEntity executionInstanceEntity) {
        executionInstance.setInstanceId(executionInstanceEntity.getId());
        executionInstance.setProcessDefinitionIdAndVersion(executionInstanceEntity.getProcessDefinitionId());
        executionInstance.setProcessInstanceId(executionInstanceEntity.getProcessInstanceId());
        executionInstance.setActivityInstanceId(executionInstanceEntity.getActivityInstanceId());
        executionInstance.setActivityId(executionInstanceEntity.getProcessDefinitionActivityId());
        executionInstance.setActive(executionInstanceEntity.isActive());
        executionInstance.setStartDate(executionInstanceEntity.getGmtCreate());
        executionInstance.setCompleteDate(executionInstanceEntity.getGmtModified());

        return executionInstance;
    }

    @Override
    public ExecutionInstance find(Long instanceId) {

        ExecutionInstanceDAO executionInstanceDAO= (ExecutionInstanceDAO) SpringContextUtil.getBean("executionInstanceDAO");
        ExecutionInstanceEntity executionInstanceEntity =    executionInstanceDAO.findOne(instanceId);
        ExecutionInstance executionInstance = new DefaultExecutionInstance();
         buildExecutionInstance(executionInstance, executionInstanceEntity);
        return executionInstance;

    }


    @Override
    public void remove(Long instanceId) {
        ExecutionInstanceDAO executionInstanceDAO= (ExecutionInstanceDAO) SpringContextUtil.getBean("executionInstanceDAO");
        executionInstanceDAO.delete(instanceId);
    }

    @Override
    public List<ExecutionInstance> findActiveExecution(String processInstanceId) {
        ExecutionInstanceDAO executionInstanceDAO= (ExecutionInstanceDAO) SpringContextUtil.getBean("executionInstanceDAO");
        executionInstanceDAO.findAllExecutionList(processInstanceId);
        return null;
    }
}
