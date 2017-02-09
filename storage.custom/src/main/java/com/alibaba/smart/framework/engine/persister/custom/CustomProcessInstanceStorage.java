package com.alibaba.smart.framework.engine.persister.custom;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.alipay.IdentityThreadLocalUtil;
import com.alibaba.smart.framework.engine.persister.alipay.InstanceSerializer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实例存储 Created by ettear on 16-4-13.
 */
public class CustomProcessInstanceStorage implements ProcessInstanceStorage {


    @Override
    public ProcessInstance insert(ProcessInstance instance) {
       return instance;
    }

    @Override
    public ProcessInstance update(ProcessInstance processInstance) {
        String string= (String)IdentityThreadLocalUtil.get();
        ProcessInstance tempProcessInstance= InstanceSerializer.deserializeAll(string);
        tempProcessInstance.setStatus(processInstance.getStatus());
        return processInstance;
    }

    @Override
    public ProcessInstance find(Long instanceId) {
        String string= (String) IdentityThreadLocalUtil.get();

        ProcessInstance processInstance = InstanceSerializer.deserializeProcessInstance(string);
        return  processInstance;
    }


    @Override
    public void remove(Long instanceId) {
        throw new EngineException("not implement intentionally");
    }
}
