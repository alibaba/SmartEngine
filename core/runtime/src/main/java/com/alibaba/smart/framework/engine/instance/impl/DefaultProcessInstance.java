package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default Process Instance
 * Created by ettear on 16-4-12.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultProcessInstance extends AbstractInstance implements ProcessInstance {
    private String processId;
    private String processVersion;
    private String parentInstanceId;
    private String parentExecutionInstanceId;
    /**
     * Running executions
     */
    private Map<String, ExecutionInstance> executions = new ConcurrentHashMap<>();

    @Override
    public void addExecution(ExecutionInstance executionInstance) {
        this.executions.put(executionInstance.getInstanceId(),executionInstance);
    }

    @Override
    public void removeExecution(String executionInstanceId) {
        this.executions.remove(executionInstanceId);
    }
}
