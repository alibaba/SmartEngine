package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultExecutionInstance;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTransitionInstance;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.TransitionInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.ExecutionInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.ExecutionInstanceEntity;

@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = ExecutionInstanceStorage.class)
public class RelationshipDatabaseExecutionInstanceStorage implements ExecutionInstanceStorage {


    @Override
    public void insert(ExecutionInstance executionInstance,
                       ProcessEngineConfiguration processEngineConfiguration) {
        ExecutionInstanceDAO executionInstanceDAO = (ExecutionInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("executionInstanceDAO");

        ExecutionInstanceEntity executionInstanceEntity = buildExecutionInstanceEntity(executionInstance);

        executionInstanceDAO.insert(executionInstanceEntity);

        Long entityId = executionInstanceEntity.getId();

        // 当数据库表id 是非自增时，需要以传入的 id 值为准
        if (0L == entityId) {
            entityId = Long.valueOf(executionInstance.getInstanceId());
        }


        executionInstanceEntity = executionInstanceDAO.findOne(entityId);

        executionInstance = buildExecutionInstance(executionInstance, executionInstanceEntity);

    }

    private ExecutionInstanceEntity buildExecutionInstanceEntity(ExecutionInstance executionInstance) {
        ExecutionInstanceEntity executionInstanceEntity = new ExecutionInstanceEntity();
        executionInstanceEntity.setId(Long.valueOf(executionInstance.getInstanceId()));
        executionInstanceEntity.setActive(executionInstance.isActive());
        executionInstanceEntity.setProcessDefinitionIdAndVersion(executionInstance.getProcessDefinitionIdAndVersion());
        executionInstanceEntity.setProcessInstanceId(Long.valueOf(executionInstance.getProcessInstanceId()));
        executionInstanceEntity.setActivityInstanceId(Long.valueOf(executionInstance.getActivityInstanceId()));
        executionInstanceEntity.setProcessDefinitionActivityId(executionInstance.getProcessDefinitionActivityId());

        Date currentDate = DateUtil.getCurrentDate();
        executionInstanceEntity.setGmtCreate(currentDate);
        executionInstanceEntity.setGmtModified(currentDate);



        //TransitionInstance incomeTransition=executionInstance.getIncomeTransition();
        //if(null!=incomeTransition){
        //    executionInstanceEntity.setIncomeTransitionId(incomeTransition.getTransitionId());
        //    executionInstanceEntity.setIncomeActivityInstanceId(Long.valueOf(incomeTransition.getSourceActivityInstanceId()));
        //
        //
        //}
        return executionInstanceEntity;
    }

    @Override
    public void update(ExecutionInstance executionInstance,
                       ProcessEngineConfiguration processEngineConfiguration) {

        ExecutionInstanceDAO executionInstanceDAO = (ExecutionInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("executionInstanceDAO");
        ExecutionInstanceEntity executionInstanceEntity = buildExecutionInstanceEntity(executionInstance);
        executionInstanceEntity.setId(Long.valueOf(executionInstance.getInstanceId()));
        executionInstanceEntity.setGmtCreate(executionInstance.getStartTime());
        executionInstanceEntity.setGmtModified(executionInstance.getCompleteTime());

        executionInstanceDAO.update(executionInstanceEntity);
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

        String incomeTransitionId = executionInstanceEntity.getIncomeTransitionId();
        Long incomeActivityInstanceId = executionInstanceEntity.getIncomeActivityInstanceId();
        if (null != incomeTransitionId || null != incomeActivityInstanceId) {
            TransitionInstance incomeTransition = new DefaultTransitionInstance();
            incomeTransition.setTransitionId(incomeTransitionId);
            incomeTransition.setSourceActivityInstanceId(incomeActivityInstanceId.toString());
            //executionInstance.setIncomeTransition(incomeTransition);
        }
        return executionInstance;
    }

    @Override
    public ExecutionInstance find(String instanceId,
                                  ProcessEngineConfiguration processEngineConfiguration) {

        ExecutionInstanceDAO executionInstanceDAO = (ExecutionInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("executionInstanceDAO");
        ExecutionInstanceEntity executionInstanceEntity = executionInstanceDAO.findOne(Long.valueOf(instanceId));
        if (executionInstanceEntity == null) {
            return null;
        }
        ExecutionInstance executionInstance = new DefaultExecutionInstance();
        buildExecutionInstance(executionInstance, executionInstanceEntity);
        return executionInstance;

    }

    @Override
    public ExecutionInstance findWithShading(String processInstanceId, String executionInstanceId,
                                             ProcessEngineConfiguration processEngineConfiguration) {
        ExecutionInstanceDAO executionInstanceDAO = (ExecutionInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("executionInstanceDAO");
        ExecutionInstanceEntity executionInstanceEntity = executionInstanceDAO.findWithShading(Long.valueOf(executionInstanceId), Long.valueOf(processInstanceId));
        ExecutionInstance executionInstance = new DefaultExecutionInstance();
        buildExecutionInstance(executionInstance, executionInstanceEntity);
        return executionInstance;
    }

    @Override
    public void remove(String instanceId,
                       ProcessEngineConfiguration processEngineConfiguration) {
        ExecutionInstanceDAO executionInstanceDAO = (ExecutionInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("executionInstanceDAO");
        executionInstanceDAO.delete(Long.valueOf(instanceId));
    }

    @Override
    public List<ExecutionInstance> findActiveExecution(String processInstanceId,
                                                       ProcessEngineConfiguration processEngineConfiguration) {
        ExecutionInstanceDAO executionInstanceDAO = (ExecutionInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("executionInstanceDAO");
        List<ExecutionInstanceEntity> executionInstanceEntities = executionInstanceDAO.findActiveExecution(Long.valueOf(processInstanceId));

        List<ExecutionInstance> executionInstanceList = Collections.emptyList();
        if (null != executionInstanceEntities) {
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
    public List<ExecutionInstance> findByActivityInstanceId(String processInstanceId, String activityInstanceId,
                                                            ProcessEngineConfiguration processEngineConfiguration) {
        ExecutionInstanceDAO executionInstanceDAO = (ExecutionInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("executionInstanceDAO");
        List<ExecutionInstanceEntity> executionInstanceEntities = executionInstanceDAO.findByActivityInstanceId(Long.valueOf(processInstanceId), Long.valueOf(activityInstanceId));
        return buildList(executionInstanceEntities);
    }

    @Override
    public List<ExecutionInstance> findAll(String processInstanceId, ProcessEngineConfiguration processEngineConfiguration) {
        ExecutionInstanceDAO executionInstanceDAO = (ExecutionInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("executionInstanceDAO");
        List<ExecutionInstanceEntity> executionInstanceEntities = executionInstanceDAO.findAllExecutionList(Long.valueOf(processInstanceId));
        return buildList(executionInstanceEntities);
    }

    private List<ExecutionInstance> buildList(List<ExecutionInstanceEntity> executionInstanceEntities){
        if (null != executionInstanceEntities) {
            List<ExecutionInstance> executionInstanceList = new ArrayList<ExecutionInstance>(executionInstanceEntities.size());
            for (ExecutionInstanceEntity executionInstanceEntity : executionInstanceEntities) {
                ExecutionInstance executionInstance = new DefaultExecutionInstance();
                buildExecutionInstance(executionInstance, executionInstanceEntity);
                executionInstanceList.add(executionInstance);
            }
            return executionInstanceList;
        }
        return new ArrayList<ExecutionInstance>();
    }

}
