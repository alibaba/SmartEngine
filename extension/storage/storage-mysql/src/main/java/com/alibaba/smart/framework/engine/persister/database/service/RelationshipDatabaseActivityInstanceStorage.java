package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultActivityInstance;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.persister.database.builder.ActivityInstanceBuilder;
import com.alibaba.smart.framework.engine.persister.database.dao.ActivityInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.ActivityInstanceEntity;

import static com.alibaba.smart.framework.engine.persister.common.constant.StorageConstant.NOT_IMPLEMENT_INTENTIONALLY;

@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = ActivityInstanceStorage.class)

public class RelationshipDatabaseActivityInstanceStorage implements ActivityInstanceStorage {

    @Override
    public void insert(ActivityInstance activityInstance,
                       ProcessEngineConfiguration processEngineConfiguration) {
        ActivityInstanceDAO activityInstanceDAO= (ActivityInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("activityInstanceDAO");

        ActivityInstanceEntity activityInstanceEntityToBePersisted = ActivityInstanceBuilder.buildActivityInstanceEntity(activityInstance);

        activityInstanceDAO.insert(activityInstanceEntityToBePersisted);

        Long entityId = activityInstanceEntityToBePersisted.getId();

        // 当数据库表id 是非自增时，需要以传入的 id 值为准
        if(0L == entityId){
            entityId = Long.valueOf(activityInstance.getInstanceId());
        }

        ActivityInstanceEntity activityInstanceEntity =  activityInstanceDAO.findOne(
            entityId,activityInstance.getTenantId());

        activityInstance.setInstanceId(activityInstanceEntity.getId().toString());
        activityInstance.setStartTime(activityInstanceEntity.getGmtCreate());

        //return activityInstance;
    }

    @Override
    public ActivityInstance update(ActivityInstance activityInstance,
                                   ProcessEngineConfiguration processEngineConfiguration) {

        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }

    @Override
    public ActivityInstance find(String instanceId,String tenantId,
                                 ProcessEngineConfiguration processEngineConfiguration) {
        ActivityInstanceDAO activityInstanceDAO= (ActivityInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("activityInstanceDAO");
        ActivityInstanceEntity activityInstanceEntity =  activityInstanceDAO.findOne(Long.valueOf(instanceId),tenantId);
        if (activityInstanceEntity == null){
            return null;
        }
        return ActivityInstanceBuilder.buildActivityInstanceFromEntity(activityInstanceEntity);
    }

    @Override
    public ActivityInstance findWithShading(String processInstanceId, String activityInstanceId,String tenantId,
            ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }

    @Override
    public void remove(String instanceId,String tenantId,
                       ProcessEngineConfiguration processEngineConfiguration) {

        ActivityInstanceDAO activityInstanceDAO= (ActivityInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("activityInstanceDAO");
        activityInstanceDAO.delete(Long.valueOf(instanceId),tenantId);

    }

    @Override
    public List<ActivityInstance> findAll(String processInstanceId,String tenantId,
                                          ProcessEngineConfiguration processEngineConfiguration) {
        ActivityInstanceDAO activityInstanceDAO= (ActivityInstanceDAO)processEngineConfiguration.getInstanceAccessor().access("activityInstanceDAO");


        List<ActivityInstanceEntity> activityInstanceEntities  = activityInstanceDAO.findAllActivity(Long.valueOf(processInstanceId),tenantId);

        if(CollectionUtil.isNotEmpty(activityInstanceEntities)){

            List<ActivityInstance> activityInstanceList= new ArrayList<ActivityInstance>(activityInstanceEntities.size());

            for (ActivityInstanceEntity activityInstanceEntity : activityInstanceEntities) {

                ActivityInstance activityInstance = ActivityInstanceBuilder.buildActivityInstanceFromEntity(activityInstanceEntity);

                activityInstanceList.add(activityInstance);
            }

            return activityInstanceList;
        }else {
            return Collections.emptyList();
        }


    }
}
