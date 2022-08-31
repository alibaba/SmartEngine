package com.alibaba.smart.framework.engine.persister.mongo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.TableSchemaStrategy;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultActivityInstance;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.persister.mongo.entity.ActivityInstanceEntity;

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
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = ActivityInstanceStorage.class)

public class MongoActivityInstanceStorage implements ActivityInstanceStorage {

    private static final String INSTANCE = "se_activity_instance";


    @Override
    public void insert(ActivityInstance activityInstance,
                       ProcessEngineConfiguration processEngineConfiguration) {
        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();

        ActivityInstanceEntity activityInstanceEntity = buildEntity(activityInstance);

        mongoTemplate.insert(activityInstanceEntity, tableSchemaStrategy.getTableSchemaFormatter(INSTANCE));

        activityInstance.setInstanceId(activityInstanceEntity.getId());
    }

    private ActivityInstanceEntity buildEntity(ActivityInstance activityInstance) {
        ActivityInstanceEntity activityInstanceEntity = new ActivityInstanceEntity();

        activityInstanceEntity.setProcessDefinitionActivityId(activityInstance.getProcessDefinitionActivityId());
        activityInstanceEntity.setProcessDefinitionIdAndVersion(activityInstance.getProcessDefinitionIdAndVersion());
        activityInstanceEntity.setProcessInstanceId(activityInstance.getProcessInstanceId());

        activityInstanceEntity.setGmtCreate(activityInstance.getStartTime());
        activityInstanceEntity.setGmtModified(activityInstance.getStartTime());
        return activityInstanceEntity;
    }

    @Override
    public ActivityInstance update(ActivityInstance activityInstance,
                                   ProcessEngineConfiguration processEngineConfiguration) {

        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();

        ActivityInstanceEntity activityInstanceEntity = buildEntity(activityInstance);

        mongoTemplate.save(activityInstanceEntity,tableSchemaStrategy.getTableSchemaFormatter(INSTANCE));
        return activityInstance;
    }

    @Override
    public ActivityInstance find(String activityInstanceId,
                                 ProcessEngineConfiguration processEngineConfiguration) {

        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();

        ActivityInstanceEntity entity =  mongoTemplate.findById(activityInstanceId,ActivityInstanceEntity.class,tableSchemaStrategy.getTableSchemaFormatter(
            INSTANCE));

        ActivityInstance activityInstance = buildInstance(entity);

        return activityInstance;
    }

    @Override
    public ActivityInstance findWithShading(String processInstanceId, String activityInstanceId,
            ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }

    private ActivityInstance buildInstance(ActivityInstanceEntity entity) {

        ActivityInstance  activityInstance = new DefaultActivityInstance();
        activityInstance.setProcessDefinitionIdAndVersion(entity.getProcessDefinitionIdAndVersion());
        activityInstance.setProcessDefinitionActivityId(entity.getProcessDefinitionActivityId());
        activityInstance.setProcessInstanceId(entity.getProcessInstanceId());
        activityInstance.setStartTime(entity.getGmtCreate());
        return activityInstance;
    }

    @Override
    public void remove(String activityInstanceId,
                       ProcessEngineConfiguration processEngineConfiguration) {

        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);


        ActivityInstanceEntity entity =  mongoTemplate.findById(activityInstanceId,ActivityInstanceEntity.class,
            collectionName);
        mongoTemplate.remove(entity,collectionName);
    }

    @Override
    public List<ActivityInstance> findAll(String processInstanceId,
                                          ProcessEngineConfiguration processEngineConfiguration) {

        MongoTemplate mongoTemplate =  (MongoTemplate)processEngineConfiguration.getInstanceAccessor().access(MONGO_TEMPLATE);
        TableSchemaStrategy tableSchemaStrategy = processEngineConfiguration.getTableSchemaStrategy();
        String collectionName = tableSchemaStrategy.getTableSchemaFormatter(INSTANCE);

        Query query = new Query();
        query.addCriteria(Criteria.where(PROCESS_INSTANCE_ID).is(processInstanceId));
        //这个地方存在逻辑不一致性, MySQL采用了按创建日期倒序，MongDB采用了按创建日期正序
        query.with( Sort.by(Sort.Direction.ASC, GMT_CREATE));

        List<ActivityInstanceEntity> activityInstanceEntityList = mongoTemplate.find(query,ActivityInstanceEntity.class,collectionName);

        if(null != activityInstanceEntityList){
            List<ActivityInstance> activityInstanceList = new ArrayList<ActivityInstance>(activityInstanceEntityList.size());

            for (ActivityInstanceEntity entity : activityInstanceEntityList) {
                ActivityInstance activityInstance = buildInstance(entity);
                activityInstanceList.add(activityInstance);
            }

            return activityInstanceList;
        }

        return Collections.emptyList();
    }
}