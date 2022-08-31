package com.alibaba.smart.framework.engine.persister.mongo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.TableSchemaStrategy;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultExecutionInstance;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.persister.mongo.entity.ExecutionInstanceEntity;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static com.alibaba.smart.framework.engine.persister.common.constant.StorageConstant.NOT_IMPLEMENT_INTENTIONALLY;
import static com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant.ACTIVITY_INSTANCE_ID;
import static com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant.GMT_CREATE;
import static com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant.MONGO_TEMPLATE;
import static com.alibaba.smart.framework.engine.persister.mongo.constant.MongoConstant.PROCESS_INSTANCE_ID;

/**
 * Created by 高海军 帝奇 74394 on 2018 October  21:52.
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = ExecutionInstanceStorage.class)

public class MongoExecutionInstanceStorage  implements ExecutionInstanceStorage {

    private static final String INSTANCE = "se_execution_instance";

    @Override
    public void insert(ExecutionInstance instance,
                       ProcessEngineConfiguration processEngineConfiguration) {


        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        ExecutionInstanceEntity entity = buildEntity(instance);

        mongoTemplate.insert(entity, collectionName);

        instance.setInstanceId(entity.getId());

    }

    private ExecutionInstanceEntity buildEntity(ExecutionInstance activityInstance) {
        ExecutionInstanceEntity executionInstanceEntity= new ExecutionInstanceEntity();

        executionInstanceEntity.setProcessInstanceId(activityInstance.getProcessInstanceId());
        executionInstanceEntity.setProcessDefinitionIdAndVersion(activityInstance.getProcessDefinitionIdAndVersion());
        executionInstanceEntity.setProcessDefinitionActivityId(activityInstance.getProcessDefinitionActivityId());
        executionInstanceEntity.setActivityInstanceId(activityInstance.getActivityInstanceId());
        executionInstanceEntity.setActive(activityInstance.isActive());

        Date currentDate = DateUtil.getCurrentDate();
        executionInstanceEntity.setGmtCreate(currentDate);
        executionInstanceEntity.setGmtModified(currentDate);
        return executionInstanceEntity;

    }

    @Override
    public void update(ExecutionInstance executionInstance,
                       ProcessEngineConfiguration processEngineConfiguration) {

        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        ExecutionInstanceEntity entity = buildEntity(executionInstance);

        mongoTemplate.save(entity,collectionName);

    }

    @Override
    public ExecutionInstance find(String executionInstanceId, ProcessEngineConfiguration processEngineConfiguration) {
        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        ExecutionInstanceEntity entity =  mongoTemplate.findById(executionInstanceId,ExecutionInstanceEntity.class,collectionName);

        ExecutionInstance activityInstance = buildInstance(entity);

        return activityInstance;

    }

    @Override
    public ExecutionInstance findWithShading(String processInstanceId, String executionInstanceId,
                                             ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);

    }

    private ExecutionInstance buildInstance(ExecutionInstanceEntity entity) {
        ExecutionInstance executionInstance= new DefaultExecutionInstance();
        executionInstance.setProcessDefinitionIdAndVersion(entity.getProcessDefinitionIdAndVersion());
        executionInstance.setProcessInstanceId(entity.getProcessInstanceId());
        executionInstance.setActive(entity.isActive());
        executionInstance.setProcessDefinitionActivityId(entity.getProcessDefinitionActivityId());
        executionInstance.setActivityInstanceId(entity.getActivityInstanceId());
        return executionInstance;
    }

    @Override
    public void remove(String executionInstanceId, ProcessEngineConfiguration processEngineConfiguration) {
        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);


        ExecutionInstanceEntity entity =  mongoTemplate.findById(executionInstanceId,ExecutionInstanceEntity.class,
            collectionName);
        mongoTemplate.remove(entity,collectionName);
    }

    @Override
    public List<ExecutionInstance> findActiveExecution(String processInstanceId,
                                                       ProcessEngineConfiguration processEngineConfiguration) {

        return findByActivityInstanceId(processInstanceId,null,processEngineConfiguration);

    }

    @Override
    public List<ExecutionInstance> findByActivityInstanceId(String processInstanceId, String activityInstanceId,
                                                            ProcessEngineConfiguration processEngineConfiguration) {
        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        Query query = new Query();
        query.addCriteria(Criteria.where(PROCESS_INSTANCE_ID).is(processInstanceId));

        if(null != activityInstanceId){
            query.addCriteria(Criteria.where(ACTIVITY_INSTANCE_ID).is(activityInstanceId));
        }

        query.with( Sort.by(Sort.Direction.ASC, GMT_CREATE));

        List<ExecutionInstanceEntity> entityList = mongoTemplate.find(query,ExecutionInstanceEntity.class,collectionName);

        if(null != entityList){
            List<ExecutionInstance> instanceList = new ArrayList<ExecutionInstance>(entityList.size());

            for (ExecutionInstanceEntity entity : entityList) {
                ExecutionInstance instance = buildInstance(entity);
                instanceList.add(instance);
            }

            return instanceList;
        }

        return Collections.emptyList();
    }

    @Override
    public List<ExecutionInstance> findAll(String processInstanceId, ProcessEngineConfiguration processEngineConfiguration) {
        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        Query query = new Query();
        query.addCriteria(Criteria.where(PROCESS_INSTANCE_ID).is(processInstanceId));

        query.with( Sort.by(Sort.Direction.ASC, GMT_CREATE));

        List<ExecutionInstanceEntity> entityList = mongoTemplate.find(query,ExecutionInstanceEntity.class,collectionName);

        if(null != entityList){
            List<ExecutionInstance> instanceList = new ArrayList<ExecutionInstance>(entityList.size());

            for (ExecutionInstanceEntity entity : entityList) {
                ExecutionInstance instance = buildInstance(entity);
                instanceList.add(instance);
            }

            return instanceList;
        }

        return Collections.emptyList();
    }
}