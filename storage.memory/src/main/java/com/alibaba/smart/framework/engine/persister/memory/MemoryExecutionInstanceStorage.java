package com.alibaba.smart.framework.engine.persister.memory;

import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实例存储 Created by ettear on 16-4-13.
 */
public class MemoryExecutionInstanceStorage implements ExecutionInstanceStorage {

    private Map<Long, ExecutionInstance> instances = new ConcurrentHashMap<Long, ExecutionInstance>();

    @Override
    public ExecutionInstance insert(ExecutionInstance instance) {
        this.instances.put(instance.getInstanceId(), instance);

        return instance;
    }

    @Override
    public ExecutionInstance update(ExecutionInstance instance) {
        this.instances.put(instance.getInstanceId(), instance);

        return instance;    }

    @Override
    public ExecutionInstance find(Long instanceId) {
        return this.instances.get(instanceId);
    }


    @Override
    public void remove(Long instanceId) {
        this.instances.remove(instanceId);
    }

    @Override
    public List<ExecutionInstance> findActiveExecution(Long processInstanceId) {
        ExecutionInstance[] activityInstances = instances.values().toArray(new ExecutionInstance[]{});
        List<ExecutionInstance> tempList =  Arrays.asList(activityInstances);

        List<ExecutionInstance> resultList = new ArrayList(tempList.size());
        for (ExecutionInstance executionInstance : tempList) {
            if(executionInstance.isActive()){
                resultList.add(executionInstance);
            }
        }

        return resultList;

     }
}
