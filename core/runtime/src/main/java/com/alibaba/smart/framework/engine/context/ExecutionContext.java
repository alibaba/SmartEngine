package com.alibaba.smart.framework.engine.context;

import java.util.Map;

import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;

/**
 * 实例上下文 Created by ettear on 16-4-11.
 */
public interface ExecutionContext {

    ProcessInstance getProcessInstance();

    void setProcessInstance(ProcessInstance processInstance);

    ExecutionInstance getCurrentExecution();

    void setCurrentExecution(ExecutionInstance executionInstance);

    PvmProcessDefinition getPvmProcessDefinition();

    void setPvmProcessDefinition(PvmProcessDefinition pvmProcessDefinition);
    
    Map<String,Object > getRequest();
    
    void setRequest(Map<String,Object > request);

    void setNeedPause(boolean needPause);

    boolean isNeedPause();
}
