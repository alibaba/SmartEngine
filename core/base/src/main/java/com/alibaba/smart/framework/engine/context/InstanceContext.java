package com.alibaba.smart.framework.engine.context;

import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;

/**
 * 实例上下文
 * Created by ettear on 16-4-11.
 */
public interface InstanceContext {

    Context getProcessContext();

    ProcessInstance getProcessInstance();

    void setProcessInstance(ProcessInstance processInstance);

    ExecutionInstance getCurrentExecution();

    void setCurrentExecution(ExecutionInstance executionInstance);
}
