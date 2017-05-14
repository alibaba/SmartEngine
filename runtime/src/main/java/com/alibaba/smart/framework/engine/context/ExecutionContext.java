package com.alibaba.smart.framework.engine.context;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
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

    ProcessInstance getParentProcessInstance();

    void setParentProcessInstance(ProcessInstance parentProcessInstance );


    PvmProcessDefinition getPvmProcessDefinition();

    void setPvmProcessDefinition(PvmProcessDefinition pvmProcessDefinition);

    Map<String, Object> getRequest();

    void setRequest(Map<String, Object> request);

    ExtensionPointRegistry getExtensionPointRegistry();

    void setExtensionPointRegistry(ExtensionPointRegistry extensionPointRegistry);

    ProcessEngineConfiguration getProcessEngineConfiguration();

    void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration);


    void setNeedPause(boolean needPause);

    boolean isNeedPause();

    //void setNested(boolean nested);
    //
    //boolean isNested();

    Long getBlockId();

    void setBlockId(Long blockId);



}
