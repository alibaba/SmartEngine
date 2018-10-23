package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.smart.framework.engine.instance.impl.DefaultExecutionInstance;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTransitionInstance;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.TransitionInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.ExecutionInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.ExecutionInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.util.SpringContextUtil;

public class RelationshipDatabaseExecutionInstanceStorage implements ExecutionInstanceStorage {


    @Override
    public ExecutionInstance insert(ExecutionInstance executionInstance) {
        ExecutionInstanceDAO executionInstanceDAO= (ExecutionInstanceDAO) SpringContextUtil.getBean("executionInstanceDAO");

        ExecutionInstanceEntity executionInstanceEntity = buildExecutionInstanceEntity(executionInstance);
        //executionInstanceEntity.setId(null);

        executionInstanceDAO.insert(executionInstanceEntity);

        executionInstanceEntity =   executionInstanceDAO.findOne(executionInstanceEntity.getId());

         executionInstance = buildExecutionInstance(executionInstance, executionInstanceEntity);
        return executionInstance;

    }

    private ExecutionInstanceEntity buildExecutionInstanceEntity(ExecutionInstance executionInstance) {
        ExecutionInstanceEntity executionInstanceEntity = new ExecutionInstanceEntity();
        executionInstanceEntity.setId(Long.valueOf(executionInstance.getInstanceId()));
        executionInstanceEntity.setActive(executionInstance.isActive());
        executionInstanceEntity.setProcessDefinitionIdAndVersion(executionInstance.getProcessDefinitionIdAndVersion());
        executionInstanceEntity.setProcessInstanceId(Long.valueOf(executionInstance.getProcessInstanceId()));
        executionInstanceEntity.setActivityInstanceId(Long.valueOf(executionInstance.getActivityInstanceId()));
        executionInstanceEntity.setProcessDefinitionActivityId(executionInstance.getProcessDefinitionActivityId());
        TransitionInstance incomeTransition=executionInstance.getIncomeTransition();
        if(null!=incomeTransition){
            executionInstanceEntity.setIncomeTransitionId(incomeTransition.getTransitionId());
            executionInstanceEntity.setIncomeActivityInstanceId(Long.valueOf(incomeTransition.getSourceActivityInstanceId()));


        }
        return executionInstanceEntity;
    }

    @Override
    public ExecutionInstance update(ExecutionInstance executionInstance) {

        ExecutionInstanceDAO executionInstanceDAO= (ExecutionInstanceDAO) SpringContextUtil.getBean("executionInstanceDAO");
        ExecutionInstanceEntity executionInstanceEntity = buildExecutionInstanceEntity(executionInstance);
        executionInstanceEntity.setId(Long.valueOf(executionInstance.getInstanceId()));
        executionInstanceEntity.setGmtCreate(executionInstance.getStartTime());
        executionInstanceEntity.setGmtModified(executionInstance.getCompleteTime());

        executionInstanceDAO.update(executionInstanceEntity);
        return executionInstance;
    }

    private ExecutionInstance buildExecutionInstance(ExecutionInstance executionInstance, ExecutionInstanceEntity executionInstanceEntity) {
        executionInstance.setInstanceId(executionInstanceEntity.getId().toString());
        executionInstance.setProcessDefinitionIdAndVersion(executionInstanceEntity.getProcessDefinitionIdAndVersion());
        executionInstance.setProcessInstanceId(executionInstanceEntity.getProcessInstanceId().toString());
        executionInstance.setActivityInstanceId(executionInstanceEntity.getActivityInstanceId().toString());
        executionInstance.setProcessDefinitionActivityId(executionInstanceEntity.getProcessDefinitionActivityId());
        executionInstance.setActive(executionInstanceEntity.isActive());
        executionInstance.setStartTime(executionInstanceEntity.getGmtCreate());
        executionInstance.setCompleteTime(executionInstanceEntity.getGmtModified());

        String incomeTransitionId=executionInstanceEntity.getIncomeTransitionId();
        Long incomeActivityInstanceId=executionInstanceEntity.getIncomeActivityInstanceId();
        if(null!=incomeTransitionId || null!=incomeActivityInstanceId){
            TransitionInstance incomeTransition= new DefaultTransitionInstance();
            incomeTransition.setTransitionId(incomeTransitionId);
            incomeTransition.setSourceActivityInstanceId(incomeActivityInstanceId.toString());
            executionInstance.setIncomeTransition(incomeTransition);
        }
        return executionInstance;
    }

    @Override
    public ExecutionInstance find(String instanceId) {

        ExecutionInstanceDAO executionInstanceDAO= (ExecutionInstanceDAO) SpringContextUtil.getBean("executionInstanceDAO");
        ExecutionInstanceEntity executionInstanceEntity =    executionInstanceDAO.findOne(Long.valueOf(instanceId));
        ExecutionInstance executionInstance = new DefaultExecutionInstance();
         buildExecutionInstance(executionInstance, executionInstanceEntity);
        return executionInstance;

    }


    @Override
    public void remove(String instanceId) {
        ExecutionInstanceDAO executionInstanceDAO= (ExecutionInstanceDAO) SpringContextUtil.getBean("executionInstanceDAO");
        executionInstanceDAO.delete(Long.valueOf(instanceId));
    }

    @Override
    public List<ExecutionInstance> findActiveExecution(String processInstanceId) {
        ExecutionInstanceDAO executionInstanceDAO= (ExecutionInstanceDAO) SpringContextUtil.getBean("executionInstanceDAO");
        List<ExecutionInstanceEntity> executionInstanceEntities=  executionInstanceDAO.findActiveExecution(Long.valueOf(processInstanceId));

        List<ExecutionInstance>  executionInstanceList = Collections.emptyList();
        if(null != executionInstanceEntities){
            executionInstanceList = new ArrayList<ExecutionInstance>(executionInstanceEntities.size());
            for (ExecutionInstanceEntity executionInstanceEntity : executionInstanceEntities) {
                ExecutionInstance executionInstance = new DefaultExecutionInstance();
                buildExecutionInstance(executionInstance, executionInstanceEntity);
                executionInstanceList.add(executionInstance);
            }
        }

        return executionInstanceList;
    }

    @Override
    public List<ExecutionInstance> findByActivityInstanceId(String processInstanceId, String activityInstanceId) {
        ExecutionInstanceDAO executionInstanceDAO= (ExecutionInstanceDAO) SpringContextUtil.getBean("executionInstanceDAO");
        List<ExecutionInstanceEntity> executionInstanceEntities=  executionInstanceDAO.findByActivityInstanceId(Long.valueOf(processInstanceId),Long.valueOf(activityInstanceId));

        List<ExecutionInstance>  executionInstanceList = null;
        if(null != executionInstanceEntities){
            executionInstanceList = new ArrayList<ExecutionInstance>(executionInstanceEntities.size());
            for (ExecutionInstanceEntity executionInstanceEntity : executionInstanceEntities) {
                ExecutionInstance executionInstance = new DefaultExecutionInstance();
                buildExecutionInstance(executionInstance, executionInstanceEntity);
                executionInstanceList.add(executionInstance);
            }
        }

        return executionInstanceList;
    }
}
