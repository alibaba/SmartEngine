package com.alibaba.smart.framework.engine.persister.database.service;

import com.alibaba.smart.framework.engine.instance.impl.DefaultProcessInstance;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.ProcessInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.ProcessInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.persister.util.SpringContextUtil;


public class RelationshipDatabaseProcessInstanceStorage implements ProcessInstanceStorage {


    @Override
    public ProcessInstance insert(ProcessInstance processInstance) {

        //TUNE
        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)SpringContextUtil.getBean("processInstanceDAO");

        ProcessInstanceEntity processInstanceEntityToBePersisted = buildProcessInstanceEntity(processInstance);
        processInstanceEntityToBePersisted.setId(null);

        processInstanceEntityToBePersisted =  processInstanceDAO.insert(processInstanceEntityToBePersisted);

        ProcessInstanceEntity processInstanceEntity1 =  processInstanceDAO.findOne(processInstanceEntityToBePersisted.getId());

        //TODO 命名不一致
        buildEntityToInstance(processInstance, processInstanceEntity1);

        return processInstance;
    }

    private ProcessInstanceEntity buildProcessInstanceEntity(ProcessInstance processInstance) {
        ProcessInstanceEntity processInstanceEntityToBePersisted = new ProcessInstanceEntity();
        processInstanceEntityToBePersisted.setId(processInstance.getInstanceId());
        processInstanceEntityToBePersisted.setGmtCreate(processInstance.getStartDate());
        processInstanceEntityToBePersisted.setGmtModified(processInstance.getCompleteDate());
        processInstanceEntityToBePersisted.setParentProcessInstanceId(processInstance.getParentInstanceId());
        processInstanceEntityToBePersisted.setStatus(processInstance.getStatus().name());
        processInstanceEntityToBePersisted.setProcessDefinitionId(processInstance.getProcessDefinitionIdAndVersion());
        return processInstanceEntityToBePersisted;
    }

    @Override
    public ProcessInstance update(ProcessInstance processInstance) {
        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)SpringContextUtil.getBean("processInstanceDAO");
        ProcessInstanceEntity processInstanceEntity = buildProcessInstanceEntity(processInstance);
        processInstanceDAO.update(processInstanceEntity);
        return processInstance;
    }

    private void buildEntityToInstance(ProcessInstance processInstance, ProcessInstanceEntity processInstanceEntity1) {
        processInstance.setProcessDefinitionIdAndVersion(processInstanceEntity1.getProcessDefinitionId());
        processInstance.setParentInstanceId(processInstanceEntity1.getParentProcessInstanceId());
        processInstance.setInstanceId(processInstanceEntity1.getId());
        processInstance.setStartDate(processInstanceEntity1.getGmtCreate());
    }

    @Override
    public ProcessInstance find(Long instanceId) {
        //TUNE :解决系统服务初始化依赖问题,避免每次获取该dao
        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)SpringContextUtil.getBean("processInstanceDAO");

        ProcessInstanceEntity processInstanceEntity = processInstanceDAO.findOne(instanceId);

        ProcessInstance processInstance  = new DefaultProcessInstance();
        processInstance.setParentInstanceId(processInstanceEntity.getParentProcessInstanceId());
        InstanceStatus processStatus = InstanceStatus.valueOf(processInstanceEntity.getStatus());
        processInstance.setStatus(processStatus);
        processInstance.setStartDate(processInstanceEntity.getGmtCreate());
        processInstance.setProcessDefinitionIdAndVersion(processInstanceEntity.getProcessDefinitionId());
        processInstance.setSuspend(InstanceStatus.suspended.equals(processStatus)  );

        //TUNE 还是叫做更新时间比较好一点,是否完成等 还是根据status 去判断.
        processInstance.setCompleteDate(processInstanceEntity.getGmtModified());
        processInstance.setInstanceId(processInstanceEntity.getId());

        return processInstance;
    }


    @Override
    public void remove(Long instanceId) {
        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)SpringContextUtil.getBean("processInstanceDAO");
        processInstanceDAO.delete(instanceId);
    }
}
