package com.alibaba.smart.framework.engine.persister.memory;

import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实例存储 Created by ettear on 16-4-13.
 */
public class MemoryProcessInstanceStorage implements ProcessInstanceStorage {

    private Map<Long, ProcessInstance> instances = new ConcurrentHashMap<Long, ProcessInstance>();

    @Override
    public ProcessInstance insert(ProcessInstance instance) {
        this.instances.put(instance.getInstanceId(), instance);

        return instance;
    }

    @Override
    public ProcessInstance update(ProcessInstance instance) {
        this.instances.put(instance.getInstanceId(), instance);

        return instance;
    }

    @Override
    public ProcessInstance findOne(Long instanceId) {
        return this.instances.get(instanceId);
    }

    @Override
    public ProcessInstance findOneForUpdate(Long instanceId) {
        return this.instances.get(instanceId);
    }

    @Override
    public List<ProcessInstance> queryProcessInstanceList(ProcessInstanceQueryParam processInstanceQueryParam) {
        return null;
    }

    @Override
    public Long count(ProcessInstanceQueryParam processInstanceQueryParam) {
        return null;
    }

    @Override
    public void remove(Long instanceId) {
        this.instances.remove(instanceId);
    }
}
