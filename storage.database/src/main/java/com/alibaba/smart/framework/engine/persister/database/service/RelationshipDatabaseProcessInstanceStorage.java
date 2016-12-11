package com.alibaba.smart.framework.engine.persister.database.service;

import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.ProcessInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.ProcessInstanceEntity;
import com.alibaba.smart.framework.engine.persister.util.SpringContextUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实例存储 Created by ettear on 16-4-13.
 */
public class RelationshipDatabaseProcessInstanceStorage implements ProcessInstanceStorage {


    @Override
    public ProcessInstance save(ProcessInstance instance) {

        ProcessInstanceDAO processInstanceDAO= (ProcessInstanceDAO)SpringContextUtil.getBean("processInstanceDAO");
        ProcessInstanceEntity processInstanceEntity = new ProcessInstanceEntity();
        processInstanceEntity.setParentProcessInstanceId(instance.getParentInstanceId());
        processInstanceEntity.setStatus(instance.getStatus().name());
        processInstanceEntity.setProcessDefinitionId(instance.getProcessUri());

        processInstanceEntity =  processInstanceDAO.insert(processInstanceEntity);

        ProcessInstanceEntity xxx =  processInstanceDAO.findOne(processInstanceEntity.getId());

        return instance;
    }

    @Override
    public ProcessInstance find(Long instanceId) {
        return null;
    }


    @Override
    public void remove(Long instanceId) {

    }
}
