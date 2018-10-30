package com.alibaba.smart.framework.engine.persister.mongo.service;

import java.util.List;

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
    public void insert(ActivityInstance activityInstance) {
        MongoTemplate mongoTemplate =  SpringContextUtil.getBean("mongoTemplate", MongoTemplate.class);
        ActivityInstanceEntity activityInstanceEntity = new ActivityInstanceEntity();
        activityInstanceEntity.setProcessDefinitionActivityId(activityInstance.getProcessDefinitionActivityId());
        activityInstanceEntity.setProcessDefinitionIdAndVersion(activityInstance.getProcessDefinitionIdAndVersion());
        activityInstanceEntity.setProcessInstanceId(activityInstance.getProcessInstanceId());
        activityInstanceEntity.setGmtCreate(activityInstance.getStartTime());
        activityInstanceEntity.setGmtModified(activityInstance.getStartTime());
        mongoTemplate.insert(activityInstanceEntity);
        activityInstanceEntity.getId();
    }

    @Override
    public ActivityInstance update(ActivityInstance activityInstance) {
        return null;
    }

    @Override
    public ActivityInstance find(String activityInstanceId) {
        return null;
    }

    @Override
    public void remove(String activityInstanceId) {

    }

    @Override
    public List<ActivityInstance> findAll(String processInstanceId) {
        return null;
    }
}