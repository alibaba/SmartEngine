package com.alibaba.smart.framework.engine.persister.database.service;

import com.alibaba.smart.framework.engine.instance.impl.DefaultActivityInstance;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.ActivityInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.ActivityInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.util.SpringContextUtil;

import java.util.ArrayList;
import java.util.List;

public class RelationshipDatabaseActivityInstanceStorage implements ActivityInstanceStorage {



    @Override
    public ActivityInstance insert(ActivityInstance activityInstance) {
        ActivityInstanceDAO activityInstanceDAO= (ActivityInstanceDAO)SpringContextUtil.getBean("activityInstanceDAO");


        ActivityInstanceEntity activityInstanceEntityToBePersisted = buildActivityInstanceEntity(activityInstance);
        activityInstanceEntityToBePersisted.setId(null);

        activityInstanceDAO.insert(activityInstanceEntityToBePersisted);

        ActivityInstanceEntity activityInstanceEntity =  activityInstanceDAO.findOne(activityInstanceEntityToBePersisted.getId());

        activityInstance.setInstanceId(activityInstanceEntity.getId());
        activityInstance.setStartTime(activityInstanceEntity.getGmtCreate());

        return activityInstance;
    }

    private ActivityInstanceEntity buildActivityInstanceEntity(ActivityInstance activityInstance) {
        ActivityInstanceEntity activityInstanceEntityToBePersisted = new ActivityInstanceEntity();
        activityInstanceEntityToBePersisted.setProcessDefinitionIdAndVersion(activityInstance.getProcessDefinitionIdAndVersion());

        //TUNE 命名不统一
        activityInstanceEntityToBePersisted.setProcessDefinitionActivityId(activityInstance.getProcessDefinitionActivityId());
        activityInstanceEntityToBePersisted.setProcessInstanceId(activityInstance.getProcessInstanceId());
        return activityInstanceEntityToBePersisted;
    }

    @Override
    public ActivityInstance update(ActivityInstance activityInstance) {
        //TUNE no need to persister
//        ActivityInstanceDAO activityInstanceDAO= (ActivityInstanceDAO)SpringContextUtil.getBean("activityInstanceDAO");
//        ActivityInstanceEntity processInstanceEntity = buildProcessInstanceEntity(processInstance);
//        activityInstanceDAO.update(processInstanceEntity);
        return activityInstance;    }

    @Override
    public ActivityInstance find(Long instanceId) {
        ActivityInstanceDAO activityInstanceDAO= (ActivityInstanceDAO)SpringContextUtil.getBean("activityInstanceDAO");
        ActivityInstanceEntity activityInstanceEntity =  activityInstanceDAO.findOne(instanceId);

        ActivityInstance activityInstance  = new DefaultActivityInstance();
        activityInstance.setStartTime(activityInstanceEntity.getGmtCreate());
        activityInstance.setProcessDefinitionIdAndVersion(activityInstanceEntity.getProcessDefinitionIdAndVersion());
        activityInstance.setInstanceId(activityInstanceEntity.getId());
        activityInstance.setProcessInstanceId(activityInstanceEntity.getProcessInstanceId());
        activityInstance.setProcessDefinitionActivityId(activityInstanceEntity.getProcessDefinitionActivityId());

        //TUNE 意义不准确?
        activityInstance.setCompleteTime(activityInstanceEntity.getGmtModified());



        return activityInstance;
    }


    @Override
    public void remove(Long instanceId) {

        ActivityInstanceDAO activityInstanceDAO= (ActivityInstanceDAO)SpringContextUtil.getBean("activityInstanceDAO");
        activityInstanceDAO.delete(instanceId);

    }

    @Override
    public List<ActivityInstance> findAll(Long processInstanceId) {
        ActivityInstanceDAO activityInstanceDAO= (ActivityInstanceDAO)SpringContextUtil.getBean("activityInstanceDAO");
        List<ActivityInstanceEntity> activityInstanceEntities  = activityInstanceDAO.findAllActivity(processInstanceId);

        List<ActivityInstance> activityInstanceList= new ArrayList<ActivityInstance>();

        for (ActivityInstanceEntity activityInstanceEntity : activityInstanceEntities) {
            ActivityInstance activityInstance = new DefaultActivityInstance();

            activityInstance.setProcessDefinitionIdAndVersion(activityInstanceEntity.getProcessDefinitionIdAndVersion());
            activityInstance.setProcessInstanceId(processInstanceId);
            activityInstance.setProcessDefinitionActivityId(activityInstanceEntity.getProcessDefinitionActivityId());
            activityInstanceList.add(activityInstance);
        }

        return activityInstanceList;
    }
}
