package com.alibaba.smart.framework.engine.persister.mongo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.TableSchemaStrategy;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultProcessInstance;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.mongo.entity.ProcessInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static com.alibaba.smart.framework.engine.persister.common.constant.StorageConstant.NOT_IMPLEMENT_INTENTIONALLY;
import static com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant.GMT_CREATE;
import static com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant.MONGO_TEMPLATE;
import static com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant.PROCESS_INSTANCE_ID;

/**
 * Created by 高海军 帝奇 74394 on 2018 October  21:52.
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = ProcessInstanceStorage.class)

public class MongoProcessInstanceStorage  implements ProcessInstanceStorage {

    private static final String INSTANCE = "se_process_instance";

    @Override
    public ProcessInstance insert(ProcessInstance instance,
                                  ProcessEngineConfiguration processEngineConfiguration) {


        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        ProcessInstanceEntity entity = buildEntity(instance);

        mongoTemplate.insert(entity, collectionName);

        instance.setInstanceId(entity.getId());

        return  instance;

    }

    private ProcessInstanceEntity buildEntity(ProcessInstance instance) {
        ProcessInstanceEntity processInstanceEntity= new ProcessInstanceEntity();
        processInstanceEntity.setProcessDefinitionIdAndVersion(instance.getProcessDefinitionIdAndVersion());
        processInstanceEntity.setStartUserId(instance.getStartUserId());
        processInstanceEntity.setParentProcessInstanceId(instance.getParentInstanceId());
        processInstanceEntity.setStatus(instance.getStatus().name());
        processInstanceEntity.setProcessDefinitionType(instance.getProcessDefinitionType());
        processInstanceEntity.setBizUniqueId(instance.getBizUniqueId());
        processInstanceEntity.setReason(instance.getReason());
        processInstanceEntity.setTitle(instance.getTitle());
        processInstanceEntity.setTag(instance.getTag());
        processInstanceEntity.setComment(instance.getComment());

        Date currentDate = DateUtil.getCurrentDate();
        processInstanceEntity.setGmtCreate(currentDate);
        processInstanceEntity.setGmtModified(currentDate);
        return processInstanceEntity;
    }

    @Override
    public ProcessInstance update(ProcessInstance instance,
                                  ProcessEngineConfiguration processEngineConfiguration) {

        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        ProcessInstanceEntity entity = buildEntity(instance);
        entity.setId(instance.getInstanceId());

        mongoTemplate.save(entity,collectionName);

        return instance;
    }

    @Override
    public ProcessInstance findOne(String instanceId, ProcessEngineConfiguration processEngineConfiguration) {
        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        ProcessInstanceEntity entity =  mongoTemplate.findById(instanceId,ProcessInstanceEntity.class,collectionName);

        ProcessInstance instance = buildInstance(entity);

        return instance;

    }

    private ProcessInstance buildInstance(ProcessInstanceEntity entity) {
        ProcessInstance processInstance= new DefaultProcessInstance();
        processInstance.setInstanceId(entity.getId());

        String processDefinitionIdAndVersion =  entity.getProcessDefinitionIdAndVersion();
        processInstance.setProcessDefinitionId(StringUtil.substringBefore(processDefinitionIdAndVersion,":"));
        processInstance.setProcessDefinitionVersion(StringUtil.substringAfter(processDefinitionIdAndVersion,":"));


        processInstance.setProcessDefinitionIdAndVersion(entity.getProcessDefinitionIdAndVersion());
        processInstance.setProcessDefinitionType(entity.getProcessDefinitionType());
        processInstance.setStartUserId(entity.getStartUserId());
        processInstance.setParentInstanceId(entity.getParentProcessInstanceId());

        //processInstance.setParentExecutionInstanceId(entity.getP);

        processInstance.setStatus(InstanceStatus.valueOf(entity.getStatus()));

        //processInstance.setSuspend(entity.);

        processInstance.setBizUniqueId(entity.getBizUniqueId());
        processInstance.setReason(entity.getReason());
        processInstance.setTag(entity.getTag());
        processInstance.setTitle(entity.getTitle());
        processInstance.setComment(entity.getComment());
        return processInstance;
    }

    @Override
    public ProcessInstance findOneForUpdate(String instanceId, ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }

    @Override
    public List<ProcessInstance> queryProcessInstanceList(ProcessInstanceQueryParam processInstanceQueryParam,
                                                          ProcessEngineConfiguration processEngineConfiguration) {
        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        Query query = buildQuery(processInstanceQueryParam);

        List<ProcessInstanceEntity> entityList = mongoTemplate.find(query,ProcessInstanceEntity.class,collectionName);


        if(null != entityList){
            List<ProcessInstance> instanceList = new ArrayList<ProcessInstance>(entityList.size());

            for (ProcessInstanceEntity entity : entityList) {
                ProcessInstance instance = buildInstance(entity);
                instanceList.add(instance);
            }

            return instanceList;
        }

        return Collections.emptyList();
    }

    private Query buildQuery(ProcessInstanceQueryParam processInstanceQueryParam) {
        Query query = new Query();

        if(null !=  processInstanceQueryParam.getProcessInstanceIdList()){
            query.addCriteria(Criteria.where(PROCESS_INSTANCE_ID).in(processInstanceQueryParam.getProcessInstanceIdList()));
        }

        if(null != processInstanceQueryParam.getBizUniqueId()){
            query.addCriteria(Criteria.where("bizUniqueId").is(processInstanceQueryParam.getBizUniqueId()));
        }

        if(null != processInstanceQueryParam.getProcessDefinitionIdAndVersion()){
            query.addCriteria(Criteria.where("processDefinitionIdAndVersion").is(processInstanceQueryParam.getProcessDefinitionIdAndVersion()));
        }

        if(null != processInstanceQueryParam.getProcessDefinitionType()){
            query.addCriteria(Criteria.where("processDefinitionType").is(processInstanceQueryParam.getProcessDefinitionType()));
        }

        if(null != processInstanceQueryParam.getStartUserId()){
            query.addCriteria(Criteria.where("startUserId").is(processInstanceQueryParam.getStartUserId()));
        }

        if(null != processInstanceQueryParam.getStatus()){
            query.addCriteria(Criteria.where("status" ).is(processInstanceQueryParam.getStatus())
           );
        }

        if(null != processInstanceQueryParam.getPageOffset()){
            Pageable pageableRequest =   PageRequest.of(processInstanceQueryParam.getPageOffset(),processInstanceQueryParam.getPageSize());
            query.with(pageableRequest);
        }

        query.with( Sort.by(Sort.Direction.ASC, GMT_CREATE));
        return query;
    }

    @Override
    public Long count(ProcessInstanceQueryParam processInstanceQueryParam,
                      ProcessEngineConfiguration processEngineConfiguration) {
        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        Query query = buildQuery(processInstanceQueryParam);

        return mongoTemplate.count(query,collectionName);
    }

    @Override
    public void remove(String processInstanceId, ProcessEngineConfiguration processEngineConfiguration) {
        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);


        ProcessInstanceEntity entity =  mongoTemplate.findById(processInstanceId,ProcessInstanceEntity.class,
            collectionName);
        mongoTemplate.remove(entity,collectionName);
    }
}