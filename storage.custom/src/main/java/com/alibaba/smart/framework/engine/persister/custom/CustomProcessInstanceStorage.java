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
 * Created by 高海军 帝奇 74394 on 2017 February  11:54.
 */
public class CustomProcessInstanceStorage implements ProcessInstanceStorage {


    @Override
    public ProcessInstance insert(ProcessInstance instance) {
       return instance;
    }

    @Override
    public ProcessInstance update(ProcessInstance processInstanceVar) {
        ProcessInstance processInstance= IdentityThreadLocalUtil.get();
        processInstance.setStatus(processInstance.getStatus());
        return processInstance;
    }

    @Override
    public ProcessInstance find(Long instanceId) {

        ProcessInstance processInstance= IdentityThreadLocalUtil.get();
        return  processInstance;
    }


    @Override
    public void remove(Long instanceId) {
        throw new EngineException("not implement intentionally");
    }
}
