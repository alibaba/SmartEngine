package com.alibaba.smart.framework.engine.context;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;

import java.util.Map;

/**
 * 实例上下文 Created by ettear on 16-4-11.
 */
public interface ExecutionContext {

    ExecutionContext getParent();

    void setParent(ExecutionContext parent);


    ProcessInstance getProcessInstance();

    void setProcessInstance(ProcessInstance processInstance);

    ExecutionInstance getExecutionInstance();

    void setExecutionInstance(ExecutionInstance executionInstance);

    ActivityInstance getActivityInstance();

    void setActivityInstance(ActivityInstance activityInstance);

    PvmProcessDefinition getPvmProcessDefinition();

    void setPvmProcessDefinition(PvmProcessDefinition pvmProcessDefinition);

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

    //void setNested(boolean nested);
    //
    //boolean isNested();

    //这个方法的正确性。是否删除 TODO

    void setSourcePvmActivity(PvmActivity sourcePvmActivity);

    PvmActivity getSourcePvmActivity();

    Long getBlockId();

    void setBlockId(Long blockId);



}
