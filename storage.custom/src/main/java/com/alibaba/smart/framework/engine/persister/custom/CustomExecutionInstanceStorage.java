package com.alibaba.smart.framework.engine.persister.custom;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.alipay.IdentityThreadLocalUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        ProcessInstance processInstance= IdentityThreadLocalUtil.get();
        List<ActivityInstance> activityInstances =  processInstance.getNewActivityInstances();

        boolean matched= false;
        for (ActivityInstance activityInstance : activityInstances) {
            ExecutionInstance tempExecutionInstance = activityInstance.getExecutionInstance();
            if(null!= tempExecutionInstance && tempExecutionInstance.getInstanceId().equals(executionInstance.getInstanceId())){

                activityInstance.setExecutionInstance(executionInstance);

                matched = true;
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

        ExecutionInstance executionInstance = null;
        ProcessInstance processInstance= IdentityThreadLocalUtil.get();
        List<ActivityInstance> activityInstances =  processInstance.getNewActivityInstances();

        boolean matched= false;
        for (ActivityInstance activityInstance : activityInstances) {
            ExecutionInstance tempExecutionInstance = activityInstance.getExecutionInstance();
            if(null!= tempExecutionInstance && tempExecutionInstance.getInstanceId().equals(instanceId)){
                executionInstance= tempExecutionInstance;
                matched = true;
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
        ProcessInstance processInstance= IdentityThreadLocalUtil.get();

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
