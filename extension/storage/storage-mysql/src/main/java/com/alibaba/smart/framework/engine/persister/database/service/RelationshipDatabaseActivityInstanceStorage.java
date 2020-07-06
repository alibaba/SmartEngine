package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultActivityInstance;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.persister.common.util.SpringContextUtil;
import com.alibaba.smart.framework.engine.persister.database.dao.ActivityInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.ActivityInstanceEntity;

import static com.alibaba.smart.framework.engine.persister.common.constant.StorageConstant.NOT_IMPLEMENT_INTENTIONALLY;

@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = ActivityInstanceStorage.class)

public class RelationshipDatabaseActivityInstanceStorage implements ActivityInstanceStorage {



    @Override
    public void insert(ActivityInstance activityInstance,
                       ProcessEngineConfiguration processEngineConfiguration) {
        ActivityInstanceDAO activityInstanceDAO= (ActivityInstanceDAO)SpringContextUtil.getBean("activityInstanceDAO");


        ActivityInstanceEntity activityInstanceEntityToBePersisted = buildActivityInstanceEntity(activityInstance);

        activityInstanceDAO.insert(activityInstanceEntityToBePersisted);

        Long entityId = activityInstanceEntityToBePersisted.getId();

        // 当数据库表id 是非自增时，需要以传入的 id 值为准
        if(0L == entityId){
            entityId = Long.valueOf(activityInstance.getInstanceId());
        }

        ActivityInstanceEntity activityInstanceEntity =  activityInstanceDAO.findOne(
            entityId);

        activityInstance.setInstanceId(activityInstanceEntity.getId().toString());
        activityInstance.setStartTime(activityInstanceEntity.getGmtCreate());

        //return activityInstance;
    }

    private ActivityInstanceEntity buildActivityInstanceEntity(ActivityInstance activityInstance) {
        ActivityInstanceEntity activityInstanceEntityToBePersisted = new ActivityInstanceEntity();
        activityInstanceEntityToBePersisted.setProcessDefinitionIdAndVersion(activityInstance.getProcessDefinitionIdAndVersion());

        activityInstanceEntityToBePersisted.setProcessDefinitionActivityId(activityInstance.getProcessDefinitionActivityId());
        activityInstanceEntityToBePersisted.setProcessInstanceId(Long.valueOf(activityInstance.getProcessInstanceId()));
        activityInstanceEntityToBePersisted.setId(Long.valueOf(activityInstance.getInstanceId()));
        return activityInstanceEntityToBePersisted;
    }

    @Override
    public ActivityInstance update(ActivityInstance activityInstance,
                                   ProcessEngineConfiguration processEngineConfiguration) {

        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }

    @Override
    public ActivityInstance find(String instanceId,
                                 ProcessEngineConfiguration processEngineConfiguration) {
        ActivityInstanceDAO activityInstanceDAO= (ActivityInstanceDAO)SpringContextUtil.getBean("activityInstanceDAO");
        ActivityInstanceEntity activityInstanceEntity =  activityInstanceDAO.findOne(Long.valueOf(instanceId));

        ActivityInstance activityInstance = buildActivityInstanceFromEntity(activityInstanceEntity);

        return activityInstance;
    }

    private ActivityInstance buildActivityInstanceFromEntity(ActivityInstanceEntity activityInstanceEntity) {
        ActivityInstance activityInstance  = new DefaultActivityInstance();

        activityInstance.setStartTime(activityInstanceEntity.getGmtCreate());
        ((DefaultActivityInstance)activityInstance).setCompleteTime(activityInstanceEntity.getGmtModified());

        activityInstance.setProcessDefinitionIdAndVersion(activityInstanceEntity.getProcessDefinitionIdAndVersion());
        activityInstance.setInstanceId(activityInstanceEntity.getId().toString());
        activityInstance.setProcessInstanceId(activityInstanceEntity.getProcessInstanceId().toString());
        activityInstance.setProcessDefinitionActivityId(activityInstanceEntity.getProcessDefinitionActivityId());
        return activityInstance;
    }

    @Override
    public void remove(String instanceId,
                       ProcessEngineConfiguration processEngineConfiguration) {

        ActivityInstanceDAO activityInstanceDAO= (ActivityInstanceDAO)SpringContextUtil.getBean("activityInstanceDAO");
        activityInstanceDAO.delete(Long.valueOf(instanceId));

    }

    @Override
    public List<ActivityInstance> findAll(String processInstanceId,
                                          ProcessEngineConfiguration processEngineConfiguration) {
        ActivityInstanceDAO activityInstanceDAO= (ActivityInstanceDAO)SpringContextUtil.getBean("activityInstanceDAO");


        List<ActivityInstanceEntity> activityInstanceEntities  = activityInstanceDAO.findAllActivity(Long.valueOf(processInstanceId));

        if(CollectionUtil.isNotEmpty(activityInstanceEntities)){

            List<ActivityInstance> activityInstanceList= new ArrayList<ActivityInstance>(activityInstanceEntities.size());

            for (ActivityInstanceEntity activityInstanceEntity : activityInstanceEntities) {

                ActivityInstance activityInstance = buildActivityInstanceFromEntity(activityInstanceEntity);

                activityInstanceList.add(activityInstance);
            }

            return activityInstanceList;
        }else {
            return Collections.emptyList();
        }


    }
}
