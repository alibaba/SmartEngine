package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultProcessInstance;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.ProcessInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.ProcessInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;

@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = ProcessInstanceStorage.class)

public class RelationshipDatabaseProcessInstanceStorage implements ProcessInstanceStorage {


    @Override
    public ProcessInstance insert(ProcessInstance processInstance,
                                  ProcessEngineConfiguration processEngineConfiguration) {

        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("processInstanceDAO");

        ProcessInstanceEntity processInstanceEntityToBePersisted = buildEntityFromInstance(processInstance);

        processInstanceDAO.insert(processInstanceEntityToBePersisted);
        Long entityId = processInstanceEntityToBePersisted.getId();


        // 当数据库表id 是非自增时，需要以传入的 id 值为准
        if(0L == entityId){
            entityId = Long.valueOf(processInstance.getInstanceId());
        }

        ProcessInstanceEntity processInstanceEntity1 =  processInstanceDAO.findOne(
            entityId);

        buildInstanceFromEntity(processInstance, processInstanceEntity1);

        //注意：不能使用下面这种方式,因为下面这张方式没有新建了ProcessInstance 对象,并没有修改原来的引用. 但是 SmartEngine 整体依赖大量的内存传参. 所以这是不能使用下面的这个 API,有点 tricky.
        //processInstance = buildProcessInstanceFromEntity(  processInstanceEntity1);

        return processInstance;
    }

    @Override
    public ProcessInstance update(ProcessInstance processInstance,
                                  ProcessEngineConfiguration processEngineConfiguration) {
        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("processInstanceDAO");
        ProcessInstanceEntity processInstanceEntity = buildEntityFromInstance(processInstance);
        processInstanceDAO.update(processInstanceEntity);
        return processInstance;
    }


    private ProcessInstanceEntity buildEntityFromInstance(ProcessInstance processInstance) {
        ProcessInstanceEntity processInstanceEntityToBePersisted = new ProcessInstanceEntity();
        processInstanceEntityToBePersisted.setId(Long.valueOf(processInstance.getInstanceId()));
        processInstanceEntityToBePersisted.setGmtCreate(processInstance.getStartTime());
        processInstanceEntityToBePersisted.setGmtModified(processInstance.getCompleteTime());

        String parentInstanceId = processInstance.getParentInstanceId();
        if(null != parentInstanceId){
            processInstanceEntityToBePersisted.setParentProcessInstanceId(Long.valueOf(parentInstanceId));
        }

        String parentExecutionInstanceId = processInstance.getParentExecutionInstanceId();
        if(null != parentExecutionInstanceId){
            processInstanceEntityToBePersisted.setParentExecutionInstanceId(Long.valueOf(parentExecutionInstanceId));
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


    private void buildInstanceFromEntity(ProcessInstance processInstance, ProcessInstanceEntity processInstanceEntity) {
        processInstance.setProcessDefinitionIdAndVersion(processInstanceEntity.getProcessDefinitionIdAndVersion());

        buildCommon(processInstance, processInstanceEntity);

        processInstance.setInstanceId(processInstanceEntity.getId().toString());
        processInstance.setStartTime(processInstanceEntity.getGmtCreate());
        processInstance.setProcessDefinitionType(processInstanceEntity.getProcessDefinitionType());
        processInstance.setBizUniqueId(processInstanceEntity.getBizUniqueId());
        processInstance.setReason(processInstanceEntity.getReason());
        processInstance.setTitle(processInstanceEntity.getTitle());
        processInstance.setTag(processInstanceEntity.getTag());
        processInstance.setComment(processInstanceEntity.getComment());
    }

    private void buildCommon(ProcessInstance processInstance, ProcessInstanceEntity processInstanceEntity) {
        Long parentProcessInstanceId = processInstanceEntity.getParentProcessInstanceId();
        if (null != parentProcessInstanceId) {
            processInstance.setParentInstanceId(parentProcessInstanceId.toString());
        }

        Long parentExecutionInstanceId = processInstanceEntity.getParentExecutionInstanceId();
        if (null != parentExecutionInstanceId) {
            processInstance.setParentExecutionInstanceId(parentExecutionInstanceId.toString());
        }
    }

    private ProcessInstance buildProcessInstanceFromEntity(ProcessInstanceEntity processInstanceEntity) {
        if (processInstanceEntity == null) {
            return null;
        }
        ProcessInstance processInstance  = new DefaultProcessInstance();

        buildCommon(processInstance, processInstanceEntity);

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
    public ProcessInstance findOne(String instanceId,
                                   ProcessEngineConfiguration processEngineConfiguration) {
        //TUNE :解决系统服务初始化依赖问题,避免每次获取该dao
        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("processInstanceDAO");

        ProcessInstanceEntity processInstanceEntity = processInstanceDAO.findOne(Long.valueOf(instanceId));
        if (processInstanceEntity == null) {
            return null;
        }
        return buildProcessInstanceFromEntity(processInstanceEntity);
    }



    @Override
    public ProcessInstance findOneForUpdate(String instanceId,
                                            ProcessEngineConfiguration processEngineConfiguration) {
        //TUNE :解决系统服务初始化依赖问题,避免每次获取该dao
        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("processInstanceDAO");

        ProcessInstanceEntity processInstanceEntity = processInstanceDAO.findOneForUpdate(Long.valueOf(instanceId));

        ProcessInstance processInstance = buildProcessInstanceFromEntity(processInstanceEntity);

        return processInstance;
    }

    @Override
    public List<ProcessInstance> queryProcessInstanceList(ProcessInstanceQueryParam processInstanceQueryParam,
                                                          ProcessEngineConfiguration processEngineConfiguration) {

        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("processInstanceDAO");

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
    public Long count(ProcessInstanceQueryParam processInstanceQueryParam,
                      ProcessEngineConfiguration processEngineConfiguration) {
        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("processInstanceDAO");
        Long processCount = processInstanceDAO.count(processInstanceQueryParam);
        return processCount;
    }


    @Override
    public void remove(String instanceId,
                       ProcessEngineConfiguration processEngineConfiguration) {
        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("processInstanceDAO");
        processInstanceDAO.delete(Long.valueOf(instanceId));
    }
}
