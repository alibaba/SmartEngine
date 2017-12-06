package com.alibaba.smart.framework.engine.modules.extensions.transaction.storage;

import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * @author Leo.yy   Created on 2017/8/3.
 * @description
 * @see
 */
public class EmptyProcessInstanceStorage implements ProcessInstanceStorage {
    @Override
    public ProcessInstance insert(ProcessInstance processInstance) {
        return null;
    }

    @Override
    public ProcessInstance update(ProcessInstance processInstance) {
        return null;
    }

    @Override
    public ProcessInstance find(Long processInstanceId) {
        return null;
    }

    @Override
    public void remove(Long processInstanceId) {
    }
}
