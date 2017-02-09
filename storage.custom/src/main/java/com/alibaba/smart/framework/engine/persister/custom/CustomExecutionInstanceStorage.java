package com.alibaba.smart.framework.engine.persister.custom;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.alipay.IdentityThreadLocalUtil;
import com.alibaba.smart.framework.engine.persister.alipay.InstanceSerializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实例存储 Created by ettear on 16-4-13.
 */
public class CustomExecutionInstanceStorage implements ExecutionInstanceStorage {

    @Override
    public ExecutionInstance insert(ExecutionInstance instance) {
        return instance;
    }

    @Override
    public ExecutionInstance update(ExecutionInstance executionInstance) {
        String string= (String)IdentityThreadLocalUtil.get();
        ProcessInstance processInstance= InstanceSerializer.deserializeAll(string);
        List<ActivityInstance> activityInstances =  processInstance.getNewActivityInstances();

        boolean matched= false;
        for (ActivityInstance activityInstance : activityInstances) {
            ExecutionInstance tempExecutionInstance = activityInstance.getExecutionInstance();
            if(tempExecutionInstance.getInstanceId().equals(executionInstance.getInstanceId())){

                activityInstance.setExecutionInstance(executionInstance);

                //TODO
                String serialize=  InstanceSerializer.serialize(processInstance);

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

        String string= (String)IdentityThreadLocalUtil.get();

        List<ExecutionInstance>  executionInstances = InstanceSerializer.deserializeExecutionInstances(string);
        for (ExecutionInstance executionInstance : executionInstances) {
            if(instanceId.equals(executionInstance.getInstanceId())){
                return executionInstance;
            }
        }
        return null;

    }


    @Override
    public void remove(Long instanceId) {
        throw new EngineException("not implement intentionally");
    }

    @Override
    public List<ExecutionInstance> findActiveExecution(Long processInstanceId) {
        String string= (String)IdentityThreadLocalUtil.get();

        List<ExecutionInstance>  executionInstances = InstanceSerializer.deserializeExecutionInstances(string);

        return executionInstances;

     }
}
