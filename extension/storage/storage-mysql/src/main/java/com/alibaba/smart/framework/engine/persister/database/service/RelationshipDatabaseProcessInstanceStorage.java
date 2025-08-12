package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultProcessInstance;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.database.builder.ProcessInstanceBuilder;
import com.alibaba.smart.framework.engine.persister.database.dao.ProcessInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.ProcessInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;

@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = ProcessInstanceStorage.class)

public class RelationshipDatabaseProcessInstanceStorage implements ProcessInstanceStorage {


    @Override
    public ProcessInstance insert(ProcessInstance processInstance,
                                  ProcessEngineConfiguration processEngineConfiguration) {

        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("processInstanceDAO");

        ProcessInstanceEntity processInstanceEntityToBePersisted = ProcessInstanceBuilder.buildEntityFromInstance(processInstance);

        processInstanceDAO.insert(processInstanceEntityToBePersisted);
        Long entityId = processInstanceEntityToBePersisted.getId();


        // 当数据库表id 是非自增时，需要以传入的 id 值为准
        if(0L == entityId){
            entityId = Long.valueOf(processInstance.getInstanceId());
        }

        ProcessInstanceEntity processInstanceEntity1 =  processInstanceDAO.findOne(
            entityId,processInstance.getTenantId());

        ProcessInstanceBuilder.buildInstanceFromEntity(processInstance, processInstanceEntity1);

        //注意：不能使用下面这种方式,因为下面这张方式没有新建了ProcessInstance 对象,并没有修改原来的引用. 但是 SmartEngine 整体依赖大量的内存传参. 所以这是不能使用下面的这个 API,有点 tricky.
        //processInstance = buildProcessInstanceFromEntity(  processInstanceEntity1);

        return processInstance;
    }

    @Override
    public ProcessInstance update(ProcessInstance processInstance,
                                  ProcessEngineConfiguration processEngineConfiguration) {
        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("processInstanceDAO");
        ProcessInstanceEntity processInstanceEntity = ProcessInstanceBuilder.buildEntityFromInstance(processInstance);
        processInstanceDAO.update(processInstanceEntity);
        return processInstance;
    }
    @Override
    public ProcessInstance findOne(String instanceId,String tenantId,
                                   ProcessEngineConfiguration processEngineConfiguration) {
        //TUNE :解决系统服务初始化依赖问题,避免每次获取该dao
        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("processInstanceDAO");

        ProcessInstanceEntity processInstanceEntity = processInstanceDAO.findOne(Long.valueOf(instanceId),tenantId);
        if (processInstanceEntity == null) {
            return null;
        }

        ProcessInstance processInstance = ProcessInstanceBuilder.buildProcessInstanceFromEntity(processInstanceEntity);
        return processInstance;
    }



    @Override
    public ProcessInstance findOneForUpdate(String instanceId,String tenantId,
                                            ProcessEngineConfiguration processEngineConfiguration) {
        //TUNE :解决系统服务初始化依赖问题,避免每次获取该dao
        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("processInstanceDAO");

        ProcessInstanceEntity processInstanceEntity = processInstanceDAO.findOneForUpdate(Long.valueOf(instanceId),tenantId);

        ProcessInstance processInstance = ProcessInstanceBuilder.buildProcessInstanceFromEntity(processInstanceEntity);
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
                ProcessInstance processInstance = ProcessInstanceBuilder.buildProcessInstanceFromEntity(processInstanceEntity);
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
    public void remove(String instanceId,String tenantId,
                       ProcessEngineConfiguration processEngineConfiguration) {
        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("processInstanceDAO");
        processInstanceDAO.delete(Long.valueOf(instanceId),tenantId);
    }
}
