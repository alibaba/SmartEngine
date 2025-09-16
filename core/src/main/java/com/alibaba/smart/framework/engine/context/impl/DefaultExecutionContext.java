package com.alibaba.smart.framework.engine.context.impl;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.util.ObjectUtil;

import lombok.Data;

import java.util.Map;

/** Created by ettear on 16-4-19. */
@Data
public class DefaultExecutionContext implements ExecutionContext {

    private ExecutionContext parent;

    private ProcessInstance processInstance;

    private ExecutionInstance executionInstance;

    private BaseElement baseElement;

    private ActivityInstance activityInstance;

    private ProcessDefinition processDefinition;

    private ProcessEngineConfiguration processEngineConfiguration;

    private Map<String, Object> request;

    private Map<String, Object> response;

    private Map<String, Object> innerExtra;

    private boolean needPause;

    @Deprecated private boolean nested;

    private String blockId; // 目前仅用于包容网关 @2024.04.25

    private String tenantId;

    @Override
    public String getTenantId() {
        if (null != tenantId) {
            return tenantId;
        }

        String tenantIdInRequest = null;
        if (null != request) {
            tenantIdInRequest =
                    ObjectUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.TENANT_ID));
        }

        if (null != tenantIdInRequest) {
            this.tenantId = tenantIdInRequest;
        }

        return tenantId;
    }

    @Override
    public void setTenantId(String tenantId) {
        if (tenantId == null) {
            return;
        }

        this.tenantId = tenantId;

        if (null != request) {
            request.put(RequestMapSpecialKeyConstant.TENANT_ID, tenantId);
        }
    }

    @Override
    public String toString() {

        String processInstanceId = null;
        if (null != processInstance) {
            processInstanceId = processInstance.getInstanceId();
        }
        String executionId = null;
        String activityId = null;
        if (null != executionInstance) {
            executionId = executionInstance.getInstanceId();
            activityId = executionInstance.getProcessDefinitionActivityId();
        }
        return activityId + ":" + processInstanceId + ":" + executionId + ":" + blockId;
    }
}
