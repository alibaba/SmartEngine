package com.alibaba.smart.framework.engine.context.impl;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
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

    //private Map<String, Object> privateContext=new HashMap<String, Object>();

    private boolean needPause;

    private boolean nested;

    private ExtensionPointRegistry extensionPointRegistry;

    private Long blockId;

    private PvmActivity sourcePvmActivity;

    @Override
    public Map<String, Object> getResponse() {
        return request;
    }

}
