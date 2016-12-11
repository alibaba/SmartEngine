package com.alibaba.smart.framework.engine.persister.database.service;

import com.alibaba.smart.framework.engine.instance.impl.DefaultActivityInstance;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.ActivityInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.dao.ProcessInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.ActivityInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.ProcessInstanceEntity;
import com.alibaba.smart.framework.engine.persister.util.SpringContextUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


public class RelationshipDatabaseActivityInstanceStorage implements ActivityInstanceStorage {



    @Override
    public ActivityInstance save(ActivityInstance activityInstance) {
        ActivityInstanceDAO activityInstanceDAO= (ActivityInstanceDAO)SpringContextUtil.getBean("activityInstanceDAO");
        ActivityInstanceEntity activityInstanceEntityToBePersisted = new ActivityInstanceEntity();
        activityInstanceEntityToBePersisted.setProcessDefinitionId(activityInstance.getProcessDefinitionIdAndVersion());

        //TUNE 命名不统一
        activityInstanceEntityToBePersisted.setProcessDefinitionActivityId(activityInstance.getActivityId());
        activityInstanceEntityToBePersisted.setProcessInstanceId(activityInstance.getProcessInstanceId());

        activityInstanceDAO.insert(activityInstanceEntityToBePersisted);

        ActivityInstanceEntity activityInstanceEntity =  activityInstanceDAO.findOne(activityInstanceEntityToBePersisted.getId());

        activityInstance.setInstanceId(activityInstanceEntity.getId());
        activityInstance.setStartDate(activityInstanceEntity.getGmtCreate());

        return activityInstance;
    }

    @Override
    public ActivityInstance find(Long instanceId) {
        ActivityInstanceDAO activityInstanceDAO= (ActivityInstanceDAO)SpringContextUtil.getBean("activityInstanceDAO");
        ActivityInstanceEntity activityInstanceEntity =  activityInstanceDAO.findOne(instanceId);

        ActivityInstance activityInstance  = new DefaultActivityInstance();
        activityInstance.setStartDate(activityInstanceEntity.getGmtCreate());
        activityInstance.setProcessDefinitionIdAndVersion(activityInstanceEntity.getProcessDefinitionId());
        activityInstance.setInstanceId(activityInstanceEntity.getId());
        activityInstance.setProcessInstanceId(activityInstanceEntity.getProcessInstanceId());
        activityInstance.setActivityId(activityInstanceEntity.getProcessDefinitionActivityId());

        //TUNE 意义不准确?
        activityInstance.setCompleteDate(activityInstanceEntity.getGmtModified());



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

        List<ActivityInstance> activityInstanceList= new ArrayList<>();

        for (ActivityInstanceEntity activityInstanceEntity : activityInstanceEntities) {
            ActivityInstance activityInstance = new DefaultActivityInstance();

            activityInstance.setProcessDefinitionIdAndVersion(activityInstanceEntity.getProcessDefinitionId());
            activityInstance.setProcessInstanceId(processInstanceId);
            activityInstance.setActivityId(activityInstanceEntity.getProcessDefinitionActivityId());
            activityInstanceList.add(activityInstance);
        }

        return activityInstanceList;
    }
}
