package com.alibaba.smart.framework.engine.context;

import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.ExecutionManager;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.instance.TaskManager;

/**
 * Created by ettear on 16-4-11.
 */
public interface InstanceContext{
    Context getProcessContext();
    Context getActivityContext();
    ProcessInstance getInstance();
    ExecutionInstance getCurrentExecution();
    ExecutionManager getExecutionManager();
    TaskManager getTaskManager();

}
