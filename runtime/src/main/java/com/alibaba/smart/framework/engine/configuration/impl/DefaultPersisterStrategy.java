package com.alibaba.smart.framework.engine.configuration.impl;

import com.alibaba.smart.framework.engine.common.persister.PersisterStrategy;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * Created by 高海军 帝奇 74394 on 2017 May  20:25.
 */
public class DefaultPersisterStrategy implements PersisterStrategy {
    @Override
    public String insert(ProcessInstance processInstance) {
        return null;
    }

    @Override
    public String update(ProcessInstance processInstance) {
        return null;
    }

    @Override
    public ProcessInstance getProcessInstance(Long processInstanceId) {
        return null;
    }

    @Override
    public ProcessInstance getProcessInstanceByExecutionInstanceId(Long executionInstanceId) {
        return null;
    }

}
