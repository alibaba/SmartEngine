package com.alibaba.smart.framework.engine.persister.mongo.service;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.persister.common.util.SpringContextUtil;
import com.alibaba.smart.framework.engine.persister.mongo.entity.ActivityInstanceEntity;

import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by 高海军 帝奇 74394 on 2018 October  21:52.
 */
public class MongoActivityInstanceStorage implements ActivityInstanceStorage {

    @Override
    public void insert(ActivityInstance activityInstance,
                       ProcessEngineConfiguration processEngineConfiguration) {
        MongoTemplate mongoTemplate =  SpringContextUtil.getBean("mongoTemplate", MongoTemplate.class);
        ActivityInstanceEntity activityInstanceEntity = new ActivityInstanceEntity();
        activityInstanceEntity.setProcessDefinitionActivityId(activityInstance.getProcessDefinitionActivityId());
        activityInstanceEntity.setProcessDefinitionIdAndVersion(activityInstance.getProcessDefinitionIdAndVersion());
        activityInstanceEntity.setProcessInstanceId(activityInstance.getProcessInstanceId());
        activityInstanceEntity.setGmtCreate(activityInstance.getStartTime());
        activityInstanceEntity.setGmtModified(activityInstance.getStartTime());
        mongoTemplate.insert(activityInstanceEntity);
    }

    @Override
    public ActivityInstance update(ActivityInstance activityInstance,
                                   ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public ActivityInstance find(String activityInstanceId,
                                 ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public void remove(String activityInstanceId,
                       ProcessEngineConfiguration processEngineConfiguration) {

    }

    @Override
    public List<ActivityInstance> findAll(String processInstanceId,
                                          ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }
}