package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.exception.ConcurrentException;
import com.alibaba.smart.framework.engine.instance.impl.DefaultProcessInstance;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.ProcessInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.ProcessInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.util.SpringContextUtil;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;

public class RelationshipDatabaseProcessInstanceStorage implements ProcessInstanceStorage {


    @Override
    public ProcessInstance insert(ProcessInstance processInstance) {

        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)SpringContextUtil.getBean("processInstanceDAO");

        ProcessInstanceEntity processInstanceEntityToBePersisted = buildProcessInstanceEntity(processInstance);
        //processInstanceEntityToBePersisted.setId(null);



        int count = processInstanceDAO.insertIgnore(processInstanceEntityToBePersisted);
        if (count == 0) {
            throw new ConcurrentException(String.format("the process already exists. bizUniqueId=[%s]",
                processInstanceEntityToBePersisted.getBizUniqueId()));
        }

        ProcessInstanceEntity processInstanceEntity1 =  processInstanceDAO.findOne(processInstanceEntityToBePersisted.getId());

        buildEntityToInstance(processInstance, processInstanceEntity1);

        //TUNE 不能使用下面这种方式,因为下面这张方式没有新建了ProcessInstance 对象,并没有修改原来的引用. 但是 SmartEngine 整体依赖大量的内存传参. 所以这是不能使用下面的这个 API,有点 tricky.
        //processInstance = buildProcessInstanceFromEntity(  processInstanceEntity1);

        return processInstance;
    }

    @Override
    public ProcessInstance update(ProcessInstance processInstance) {
        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)SpringContextUtil.getBean("processInstanceDAO");
        ProcessInstanceEntity processInstanceEntity = buildProcessInstanceEntity(processInstance);
        processInstanceDAO.update(processInstanceEntity);
        return processInstance;
    }


    private ProcessInstanceEntity buildProcessInstanceEntity(ProcessInstance processInstance) {
        ProcessInstanceEntity processInstanceEntityToBePersisted = new ProcessInstanceEntity();
        processInstanceEntityToBePersisted.setId(processInstance.getInstanceId());
        processInstanceEntityToBePersisted.setGmtCreate(processInstance.getStartTime());
        processInstanceEntityToBePersisted.setGmtModified(processInstance.getCompleteTime());
        processInstanceEntityToBePersisted.setParentProcessInstanceId(processInstance.getParentInstanceId());
        processInstanceEntityToBePersisted.setStatus(processInstance.getStatus().name());
        processInstanceEntityToBePersisted.setProcessDefinitionIdAndVersion(processInstance.getProcessDefinitionIdAndVersion());
        processInstanceEntityToBePersisted.setStartUserId(processInstance.getStartUserId());
        processInstanceEntityToBePersisted.setProcessDefinitionType(processInstance.getProcessDefinitionType());
        processInstanceEntityToBePersisted.setBizUniqueId(processInstance.getBizUniqueId());
        processInstanceEntityToBePersisted.setReason(processInstance.getReason());
        return processInstanceEntityToBePersisted;
    }


    private void buildEntityToInstance(ProcessInstance processInstance, ProcessInstanceEntity processInstanceEntity) {
        processInstance.setProcessDefinitionIdAndVersion(processInstanceEntity.getProcessDefinitionIdAndVersion());
        processInstance.setParentInstanceId(processInstanceEntity.getParentProcessInstanceId());
        processInstance.setInstanceId(processInstanceEntity.getId());
        processInstance.setStartTime(processInstanceEntity.getGmtCreate());
        processInstance.setProcessDefinitionType(processInstanceEntity.getProcessDefinitionType());
        processInstance.setBizUniqueId(processInstanceEntity.getBizUniqueId());
        processInstance.setReason(processInstanceEntity.getReason());
    }

    private ProcessInstance buildProcessInstanceFromEntity(ProcessInstanceEntity processInstanceEntity) {
        ProcessInstance processInstance  = new DefaultProcessInstance();
        processInstance.setParentInstanceId(processInstanceEntity.getParentProcessInstanceId());
        InstanceStatus processStatus = InstanceStatus.valueOf(processInstanceEntity.getStatus());
        processInstance.setStatus(processStatus);
        processInstance.setStartTime(processInstanceEntity.getGmtCreate());
        String processDefinitionIdAndVersion = processInstanceEntity.getProcessDefinitionIdAndVersion();
        processInstance.setProcessDefinitionIdAndVersion(processDefinitionIdAndVersion);
        processInstance.setProcessDefinitionId(StringUtil.substringBefore(processDefinitionIdAndVersion,":"));
        processInstance.setProcessDefinitionVersion(StringUtil.substringAfter(processDefinitionIdAndVersion,":"));
        processInstance.setSuspend(InstanceStatus.suspended.equals(processStatus)  );
        processInstance.setStartUserId(processInstanceEntity.getStartUserId());
        processInstance.setProcessDefinitionType(processInstanceEntity.getProcessDefinitionType());
        processInstance.setReason(processInstanceEntity.getReason());
        processInstance.setBizUniqueId(processInstanceEntity.getBizUniqueId());


        //TUNE 还是叫做更新时间比较好一点,是否完成等 还是根据status 去判断.
        processInstance.setCompleteTime(processInstanceEntity.getGmtModified());
        processInstance.setInstanceId(processInstanceEntity.getId());
        return processInstance;
    }

    @Override
    public ProcessInstance findOne(Long instanceId) {
        //TUNE :解决系统服务初始化依赖问题,避免每次获取该dao
        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)SpringContextUtil.getBean("processInstanceDAO");

        ProcessInstanceEntity processInstanceEntity = processInstanceDAO.findOne(instanceId);

        ProcessInstance processInstance = buildProcessInstanceFromEntity(processInstanceEntity);

        return processInstance;
    }



    @Override
    public List<ProcessInstance> queryProcessInstanceList(ProcessInstanceQueryParam processInstanceQueryParam) {

        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)SpringContextUtil.getBean("processInstanceDAO");

        List<ProcessInstanceEntity> processInstanceEntities = processInstanceDAO.find(processInstanceQueryParam);

        List<ProcessInstance> processInstanceList = null;
        if(null != processInstanceEntities){
            processInstanceList = new ArrayList<ProcessInstance>(processInstanceEntities.size());
            for (ProcessInstanceEntity processInstanceEntity : processInstanceEntities) {
                ProcessInstance processInstance = buildProcessInstanceFromEntity(processInstanceEntity);
                processInstanceList.add(processInstance);
            }
        }
        
        return processInstanceList;
    }



    @Override
    public void remove(Long instanceId) {
        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)SpringContextUtil.getBean("processInstanceDAO");
        processInstanceDAO.delete(instanceId);
    }
}
