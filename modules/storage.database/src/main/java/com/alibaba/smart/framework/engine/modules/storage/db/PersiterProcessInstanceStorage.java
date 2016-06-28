package com.alibaba.smart.framework.engine.modules.storage.db;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;

/**
 * 内存实例存储 Created by ettear on 16-4-13.
 */
public class PersiterProcessInstanceStorage implements ProcessInstanceStorage {

    private Map<String, ProcessInstance> instances           = new ConcurrentHashMap<>();
    private Map<String, ProcessInstance> subProcessInstances = new ConcurrentHashMap<>();

    @Override
    public ProcessInstance save(ProcessInstance instance) {
        this.instances.put(instance.getInstanceId(), instance);
        if (null != instance.getParentActivityInstanceId()) {
            this.subProcessInstances.put(instance.getParentActivityInstanceId(), instance);
        }
        return instance;
    }

    @Override
    public ProcessInstance find(String instanceId) {
        return this.instances.get(instanceId);
    }

    @Override
    public ProcessInstance findSubProcess(String activityInstanceId) {
        return this.subProcessInstances.get(activityInstanceId);
    }

    @Override
    public void remove(String instanceId) {
        this.instances.remove(instanceId);
    }
}
