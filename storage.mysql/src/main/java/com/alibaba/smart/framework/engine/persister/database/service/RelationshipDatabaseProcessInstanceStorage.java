package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.exception.ConcurrentException;
import com.alibaba.smart.framework.engine.instance.impl.DefaultProcessInstance;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.common.util.SpringContextUtil;
import com.alibaba.smart.framework.engine.persister.database.dao.ProcessInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.ProcessInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;

public class RelationshipDatabaseProcessInstanceStorage implements ProcessInstanceStorage {


    @Override
    public ProcessInstance insert(ProcessInstance processInstance) {

        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)SpringContextUtil.getBean("processInstanceDAO");

        ProcessInstanceEntity processInstanceEntityToBePersisted = buildProcessInstanceEntity(processInstance);
        //processInstanceEntityToBePersisted.setId(null);



        int count = processInstanceDAO.insertIgnore(processInstanceEntityToBePersisted);
        if (count == 0) {
            throw new ConcurrentException(String.format("the process already exists. processInstanceId=[%s], bizUniqueId=[%s]",
                processInstanceEntityToBePersisted.getId(), processInstanceEntityToBePersisted.getBizUniqueId()));
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
        processInstanceEntityToBePersisted.setId(Long.valueOf(processInstance.getInstanceId()));
        processInstanceEntityToBePersisted.setGmtCreate(processInstance.getStartTime());
        processInstanceEntityToBePersisted.setGmtModified(processInstance.getCompleteTime());
        String parentInstanceId = processInstance.getParentInstanceId();
        if(null != parentInstanceId){
            processInstanceEntityToBePersisted.setParentProcessInstanceId(Long.valueOf(parentInstanceId));
        }
        processInstanceEntityToBePersisted.setStatus(processInstance.getStatus().name());
        processInstanceEntityToBePersisted.setProcessDefinitionIdAndVersion(processInstance.getProcessDefinitionIdAndVersion());
        processInstanceEntityToBePersisted.setStartUserId(processInstance.getStartUserId());
        processInstanceEntityToBePersisted.setProcessDefinitionType(processInstance.getProcessDefinitionType());
        processInstanceEntityToBePersisted.setBizUniqueId(processInstance.getBizUniqueId());
        processInstanceEntityToBePersisted.setReason(processInstance.getReason());
        processInstanceEntityToBePersisted.setTitle(processInstance.getTitle());
        processInstanceEntityToBePersisted.setComment(processInstance.getComment());
        processInstanceEntityToBePersisted.setTag(processInstance.getTag());
        return processInstanceEntityToBePersisted;
    }


    private void buildEntityToInstance(ProcessInstance processInstance, ProcessInstanceEntity processInstanceEntity) {
        processInstance.setProcessDefinitionIdAndVersion(processInstanceEntity.getProcessDefinitionIdAndVersion());
        Long parentProcessInstanceId = processInstanceEntity.getParentProcessInstanceId();
        if(null != parentProcessInstanceId){
            processInstance.setParentInstanceId(parentProcessInstanceId.toString());
        }
        processInstance.setInstanceId(processInstanceEntity.getId().toString());
        processInstance.setStartTime(processInstanceEntity.getGmtCreate());
        processInstance.setProcessDefinitionType(processInstanceEntity.getProcessDefinitionType());
        processInstance.setBizUniqueId(processInstanceEntity.getBizUniqueId());
        processInstance.setReason(processInstanceEntity.getReason());
        processInstance.setTitle(processInstanceEntity.getTitle());
        processInstance.setTag(processInstanceEntity.getTag());
        processInstance.setComment(processInstanceEntity.getComment());
    }

    private ProcessInstance buildProcessInstanceFromEntity(ProcessInstanceEntity processInstanceEntity) {
        if (processInstanceEntity == null) {
            return null;
        }
        ProcessInstance processInstance  = new DefaultProcessInstance();
        Long parentProcessInstanceId = processInstanceEntity.getParentProcessInstanceId();

        if(null != parentProcessInstanceId){
            processInstance.setParentInstanceId(parentProcessInstanceId.toString());
        }
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
        processInstance.setInstanceId(processInstanceEntity.getId().toString());
        processInstance.setTag(processInstanceEntity.getTag());
        processInstance.setTitle(processInstanceEntity.getTitle());
        processInstance.setComment(processInstanceEntity.getComment());
        return processInstance;
    }

    @Override
    public ProcessInstance findOne(String instanceId) {
        //TUNE :解决系统服务初始化依赖问题,避免每次获取该dao
        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)SpringContextUtil.getBean("processInstanceDAO");

        ProcessInstanceEntity processInstanceEntity = processInstanceDAO.findOne(Long.valueOf(instanceId));

        ProcessInstance processInstance = buildProcessInstanceFromEntity(processInstanceEntity);

        return processInstance;
    }



    @Override
    public ProcessInstance findOneForUpdate(String instanceId) {
        //TUNE :解决系统服务初始化依赖问题,避免每次获取该dao
        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)SpringContextUtil.getBean("processInstanceDAO");

        ProcessInstanceEntity processInstanceEntity = processInstanceDAO.findOneForUpdate(Long.valueOf(instanceId));

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
    public Long count(ProcessInstanceQueryParam processInstanceQueryParam) {
        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)SpringContextUtil.getBean("processInstanceDAO");
        Long processCount = processInstanceDAO.count(processInstanceQueryParam);
        return processCount;
    }


    @Override
    public void remove(String instanceId) {
        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)SpringContextUtil.getBean("processInstanceDAO");
        processInstanceDAO.delete(Long.valueOf(instanceId));
    }
}