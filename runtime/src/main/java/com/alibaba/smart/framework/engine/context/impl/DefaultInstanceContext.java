package com.alibaba.smart.framework.engine.context.impl;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * DefaultInstanceContext Created by ettear on 16-4-19.
 */
@Data
public class DefaultInstanceContext implements ExecutionContext {

    private ExecutionContext parent;

    private ProcessInstance processInstance;

    private ExecutionInstance executionInstance;

    private ActivityInstance activityInstance;

    private PvmProcessDefinition pvmProcessDefinition;

    private ProcessEngineConfiguration processEngineConfiguration;

    private Map<String, Object> request;

    private Map<String, Object> privateContext=new HashMap<String, Object>();

    private boolean needPause;

    private boolean nested;

    private ExtensionPointRegistry extensionPointRegistry;

    private Long blockId;

    private PvmActivity sourcePvmActivity;

    private boolean needParallelLock = false;

    @Override
    public Map<String, Object> getResponse() {
        return request;
    }

    public static DefaultInstanceContext copy(ExecutionContext context) {
        DefaultInstanceContext copyContext = new DefaultInstanceContext();
        copyContext.parent = context.getParent();
        copyContext.processInstance = context.getProcessInstance();
        copyContext.executionInstance = context.getExecutionInstance();
        copyContext.activityInstance = context.getActivityInstance();
        copyContext.pvmProcessDefinition = context.getPvmProcessDefinition();
        copyContext.processEngineConfiguration = context.getProcessEngineConfiguration();
        copyContext.request = context.getRequest();
        copyContext.privateContext = context.getPrivateContext();
        copyContext.needPause = context.isNeedPause();
        copyContext.extensionPointRegistry = context.getExtensionPointRegistry();
        copyContext.blockId = context.getBlockId();
        copyContext.sourcePvmActivity = context.getSourcePvmActivity();
        return copyContext;
    }

}
