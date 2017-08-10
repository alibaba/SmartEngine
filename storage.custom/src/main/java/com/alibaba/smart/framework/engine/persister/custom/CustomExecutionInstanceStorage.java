package com.alibaba.smart.framework.engine.persister.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        Collection<ProcessInstance> processInstances = PersisterSession.currentSession().getProcessInstances().values();

        boolean matched= false;

        for (ProcessInstance processInstance : processInstances) {
            List<ActivityInstance> activityInstances = processInstance.getNewActivityInstances();

            for (ActivityInstance activityInstance : activityInstances) {
                ExecutionInstance tempExecutionInstance = activityInstance.getExecutionInstance();
                if (null != tempExecutionInstance && tempExecutionInstance.getInstanceId().equals(
                    executionInstance.getInstanceId())) {

                    activityInstance.setExecutionInstance(executionInstance);

                    matched = true;
                    break;

                }
            }
            if (matched) {
                break;
            }
        }

        if(!matched){
            throw new EngineException("No ExecutionInstance found : "+executionInstance);
        }

        return executionInstance;
    }

    @Override
    public ExecutionInstance find(Long instanceId) {

        Collection<ProcessInstance> processInstances = PersisterSession.currentSession().getProcessInstances().values();

        boolean matched = false;

        ExecutionInstance executionInstance = null;

        for (ProcessInstance processInstance : processInstances) {

            List<ActivityInstance> activityInstances = processInstance.getNewActivityInstances();

            if (null == activityInstances || activityInstances.isEmpty()) {

                // do nothing , cause exception.
            } else {
                int size = activityInstances.size();
                for (int i = size - 1; i >= 0; i--) {
                    ActivityInstance activityInstance = activityInstances.get(i);
                    ExecutionInstance tempExecutionInstance = activityInstance.getExecutionInstance();
                    if (null != tempExecutionInstance && tempExecutionInstance.getInstanceId().equals(instanceId)) {
                        executionInstance = tempExecutionInstance;
                        matched = true;
                        break;

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

        List<ActivityInstance> activityInstances =  processInstance.getNewActivityInstances();

        //TUNE 扩容
        List<ExecutionInstance> executionInstances = new ArrayList<ExecutionInstance>(activityInstances.size());
        for (ActivityInstance activityInstance : activityInstances) {
            ExecutionInstance executionInstance =   activityInstance.getExecutionInstance();
            if(null != executionInstance && executionInstance.isActive()){
                executionInstances.add(executionInstance);
            }
        }

        return executionInstances;


     }
}
