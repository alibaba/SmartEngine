package com.alibaba.smart.framework.engine.context;

import java.util.Map;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;

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

    Map<String, Object> getPrivateContext();

    void setPrivateContext(Map<String, Object> request);

    void setNeedPause(boolean needPause);

    boolean isNeedPause();

    //void setNested(boolean nested);
    //
    //boolean isNested();

    void setSourcePvmActivity(PvmActivity sourcePvmActivity);

    PvmActivity getSourcePvmActivity();

    Long getBlockId();

    void setBlockId(Long blockId);



}
