package com.alibaba.smart.framework.engine.instance;

import java.util.List;
import java.util.Map;

/**
 * 流程实例
 * Created by ettear on 16-4-12.
 */
public interface ProcessInstance extends Instance {
    String getProcessId();

    void setProcessId(String processId);

    String getProcessVersion();

    void setProcessVersion(String processVersion);

    String getParentInstanceId();

    void setParentInstanceId(String parentInstanceId);

    String getParentExecutionInstanceId();

    void setParentExecutionInstanceId(String parentExecutionInstanceId);

    /**
     * 获取流程的执行实例
     * @return 流程执行实例
     */
    Map<String,ExecutionInstance> getExecutions();

    void addExecution(ExecutionInstance executionInstance);

    void removeExecution(String executionInstanceId);
}
