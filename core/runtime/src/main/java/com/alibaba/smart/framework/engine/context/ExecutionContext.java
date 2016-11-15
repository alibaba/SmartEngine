package com.alibaba.smart.framework.engine.context;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;

import java.util.Map;

/**
 * 实例上下文 Created by ettear on 16-4-11.
 */
public interface ExecutionContext {

    ProcessInstance getProcessInstance();

    void setProcessInstance(ProcessInstance processInstance);

//    ExecutionInstance getCurrentExecution();
//
//    void setCurrentExecution(ExecutionInstance executionInstance);

    PvmProcessDefinition getPvmProcessDefinition();

    void setPvmProcessDefinition(PvmProcessDefinition pvmProcessDefinition);

    Map<String, Object> getRequest();

    void setRequest(Map<String, Object> request);

    void setNeedPause(boolean needPause);

    boolean isNeedPause();

    ExtensionPointRegistry getExtensionPointRegistry();

    void setExtensionPointRegistry(ExtensionPointRegistry extensionPointRegistry);
}
