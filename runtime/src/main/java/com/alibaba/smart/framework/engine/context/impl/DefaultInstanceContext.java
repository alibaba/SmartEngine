package com.alibaba.smart.framework.engine.context.impl;

import java.util.Map;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

import lombok.Data;

/**
 * DefaultInstanceContext Created by ettear on 16-4-19.
 */
@Data
public class DefaultInstanceContext implements ExecutionContext {

    private ExecutionContext parent;

    private ProcessInstance processInstance;

    private ExecutionInstance executionInstance;

    private ActivityInstance activityInstance;

    private ProcessDefinition processDefinition;

    private ProcessEngineConfiguration processEngineConfiguration;

    private Map<String, Object> request;

    private Map<String, Object> response;

    private boolean needPause;

    private boolean nested;

    private ExtensionPointRegistry extensionPointRegistry;

    private Long blockId;


}
