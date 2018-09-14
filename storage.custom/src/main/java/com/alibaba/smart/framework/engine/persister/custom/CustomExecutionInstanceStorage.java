package com.alibaba.smart.framework.engine.persister.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.InstanceUtil;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  11:54.
 */
public class CustomExecutionInstanceStorage implements ExecutionInstanceStorage {

    @Override
    public ExecutionInstance insert(ExecutionInstance instance) {
        return instance;
    }

    @Override
    public ExecutionInstance update(ExecutionInstance executionInstance) {
        //Collection<ProcessInstance> processInstances = PersisterSession.currentSession().getProcessInstances().values();
        //
        //boolean matched= false;
        //
        //for (ProcessInstance processInstance : processInstances) {
        //    List<ActivityInstance> activityInstances = processInstance.getActivityInstances();
        //
        //    for (ActivityInstance activityInstance : activityInstances) {
        //        List<ExecutionInstance> executionInstances =    activityInstance.getExecutionInstanceList();
        //        for (ExecutionInstance tempExecutionInstance : executionInstances) {
        //            if (null != tempExecutionInstance && tempExecutionInstance.getInstanceId().equals(
        //                executionInstance.getInstanceId())) {
        //
        //                //TODO check logic
        //                tempExecutionInstance = executionInstance;
        //                //activityInstance.setExecutionInstance(executionInstance);
        //
        //                matched = true;
        //                break;
        //
        //            }
        //        }
        //
        //    }
        //    if (matched) {
        //        break;
        //    }
        //}
        //
        //if(!matched){
        //    throw new EngineException("No ExecutionInstance found : "+executionInstance);
        //}

        return executionInstance;
    }

    @Override
    public ExecutionInstance find(Long instanceId) {

        Collection<ProcessInstance> processInstances = PersisterSession.currentSession().getProcessInstances().values();

        boolean matched = false;

        ExecutionInstance executionInstance = null;

        for (ProcessInstance processInstance : processInstances) {

            List<ActivityInstance> activityInstances = processInstance.getActivityInstances();

            if (null == activityInstances || activityInstances.isEmpty()) {

                // do nothing , cause exception.
            } else {
                int size = activityInstances.size();
                for (int i = size - 1; i >= 0; i--) {
                    ActivityInstance activityInstance = activityInstances.get(i);

                    List<ExecutionInstance> executionInstances =    activityInstance.getExecutionInstanceList();
                    for (ExecutionInstance tempExecutionInstance : executionInstances) {
                        if (null != tempExecutionInstance && tempExecutionInstance.getInstanceId().equals(instanceId)) {
                            executionInstance = tempExecutionInstance;
                            matched = true;
                            break;

                        }
                    }

                }

            }
            if (matched) {
                break;
            }
        }

        if(!matched){
            throw new EngineException("No ExecutionInstance found for id : "+instanceId);
        }

        return executionInstance;

    }


    @Override
    public void remove(Long instanceId) {
        throw new EngineException("not implement intentionally");
    }

    @Override
    public List<ExecutionInstance> findActiveExecution(Long processInstanceId) {
        ProcessInstance processInstance = PersisterSession.currentSession().getProcessInstance(processInstanceId);
        if(null==processInstance){
            return Collections.emptyList();
        }

        return  InstanceUtil.findActiveExecution(processInstance);

     }

    @Override
    public List<ExecutionInstance> findByActivityInstanceId(Long processInstanceId, Long activityInstanceId) {
        ProcessInstance processInstance = PersisterSession.currentSession().getProcessInstance(processInstanceId);
        if (null == processInstance) {
            return null;
        }
        List<ActivityInstance> activityInstances = processInstance.getActivityInstances();
        if (null == activityInstances) {
            return null;
        }
        //TUNE 扩容
        List<ExecutionInstance> executionInstances = new ArrayList<ExecutionInstance>(activityInstances.size());
        for (ActivityInstance activityInstance : activityInstances) {
            if (activityInstance.getInstanceId().equals(activityInstanceId)) {
                return activityInstance.getExecutionInstanceList();
            }
        }
        return null;
    }
}
