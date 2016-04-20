package com.alibaba.smart.framework.engine.context;

import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.manager.ExecutionInstanceManager;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.instance.manager.TaskManager;

/**
 * Created by ettear on 16-4-11.
 */
public interface InstanceContext{
    //Context getProcessContext();
    //Context getActivityContext();
    ProcessInstance getProcessInstance();
    void setProcessInstance(ProcessInstance processInstance);
    ExecutionInstance getCurrentExecution();
    void setCurrentExecution(ExecutionInstance executionInstance);
    //ExecutionInstanceManager getExecutionManager();
    //TaskManager getTaskManager();

}
