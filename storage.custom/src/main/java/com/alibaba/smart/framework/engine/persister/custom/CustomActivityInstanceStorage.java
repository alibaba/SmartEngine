package com.alibaba.smart.framework.engine.persister.custom;

import java.util.Collection;
import java.util.List;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  11:54.
 */
public class CustomActivityInstanceStorage implements ActivityInstanceStorage {


    @Override
    public ActivityInstance insert(ActivityInstance instance) {
       return instance;
    }

    @Override
    public ActivityInstance update(ActivityInstance instance) {
        throw new EngineException("not implement intentionally");
    }

    @Override
    public ActivityInstance find(Long activityInstanceId) {
        Collection<ProcessInstance> processInstances = PersisterSession.currentSession().getProcessInstances().values();

        boolean matched = false;
        ActivityInstance matchedActivityInstance = null;

        for (ProcessInstance processInstance : processInstances) {
            List<ActivityInstance> activityInstances = processInstance.getNewActivityInstances();

            for (ActivityInstance activityInstance : activityInstances) {
                if (activityInstance.getInstanceId().equals(activityInstanceId)) {
                    matched= true;
                    matchedActivityInstance = activityInstance;
                    break;
                }
            }
            if(matched){
                break;
            }
        }
        //TODO why? review by ettear
        //if(null != matchedActivityInstance){
        //    throw new EngineException("No ActivityInstance found : "+activityInstanceId);
        //}

        return matchedActivityInstance;
    }


    @Override
    public void remove(Long instanceId) {
        throw new EngineException("not implement intentionally");
    }

    @Override
    public List<ActivityInstance> findAll(Long processInstanceId) {
        ProcessInstance processInstance= PersisterSession.currentSession().getProcessInstance(processInstanceId);
        return null == processInstance ? null : processInstance.getNewActivityInstances();
    }
}
