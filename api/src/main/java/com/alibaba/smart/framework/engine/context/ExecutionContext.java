package com.alibaba.smart.framework.engine.context;

import java.util.Map;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;


public interface ExecutionContext {

    ExecutionContext getParent();

    void setParent(ExecutionContext parent);


    ProcessInstance getProcessInstance();

    void setProcessInstance(ProcessInstance processInstance);

    ExecutionInstance getExecutionInstance();

    void setExecutionInstance(ExecutionInstance executionInstance);

    ActivityInstance getActivityInstance();

    void setActivityInstance(ActivityInstance activityInstance);

    ProcessDefinition getProcessDefinition();

    void setProcessDefinition(ProcessDefinition processDefinition);

    Map<String, Object> getRequest();

    void setRequest(Map<String, Object> request);

    void setResponse(Map<String, Object> response);


    Map<String, Object> getResponse();

    ExtensionPointRegistry getExtensionPointRegistry();

    void setExtensionPointRegistry(ExtensionPointRegistry extensionPointRegistry);

    ProcessEngineConfiguration getProcessEngineConfiguration();

    void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration);


    void setNeedPause(boolean needPause);

    boolean isNeedPause();


    Long getBlockId();

    void setBlockId(Long blockId);



}
