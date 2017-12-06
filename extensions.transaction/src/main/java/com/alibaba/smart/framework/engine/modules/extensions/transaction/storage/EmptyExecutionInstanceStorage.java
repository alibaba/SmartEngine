package com.alibaba.smart.framework.engine.modules.extensions.transaction.storage;

import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;

import java.util.List;

/**
 * @author Leo.yy   Created on 2017/8/3.
 * @description
 * @see
 */
public class EmptyExecutionInstanceStorage implements ExecutionInstanceStorage {
    @Override
    public ExecutionInstance insert(ExecutionInstance executionInstance) {
        return null;
    }

    @Override
    public ExecutionInstance update(ExecutionInstance executionInstance) {
        return null;
    }

    @Override
    public ExecutionInstance find(Long executionInstanceId) {
        return null;
    }

    @Override
    public void remove(Long executionInstanceId) {

    }

    @Override
    public List<ExecutionInstance> findActiveExecution(Long processInstanceId) {
        return null;
    }

    @Override
    public List<ExecutionInstance> findByActivityInstanceId(Long processInstanceId, Long activityInstanceId) {
        return null;
    }
}
