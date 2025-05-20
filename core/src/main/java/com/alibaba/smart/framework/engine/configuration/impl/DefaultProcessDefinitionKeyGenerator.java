package com.alibaba.smart.framework.engine.configuration.impl;

import com.alibaba.smart.framework.engine.common.util.IdAndVersionUtil;
import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;
import com.alibaba.smart.framework.engine.configuration.ProcessDefinitionKeyGenerator;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.option.ProcessDefinitionMultiTenantModeOption;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;


/**
 * 流程定义是否是多租户模式：默认是多租户模式<br>
 * 主要应用场景：<br>
 * 1. 流程定义和流程实例数据都是多租户模式：每个企业都有自己的业务流程定义${id}:${version}:${tenantId}<br>
 * 2. 流程定义是全局的，但是，流程实例是多租户模式 ，流程定义的key：${id}:${version}<br>
 *
 * @author yanricheng
 * @date 2025/5/20
 */
public class DefaultProcessDefinitionKeyGenerator implements ProcessDefinitionKeyGenerator {

    protected ProcessEngineConfiguration processEngineConfiguration;

    public DefaultProcessDefinitionKeyGenerator(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }

    @Override
    public void generate(ProcessInstance instance) {
        if (instance == null) {
            return;
        }
        instance.setUniqueProcessDefinitionIdAndVersion(buildProcessDefinitionUniqueKey(instance.getProcessDefinitionId(), instance.getProcessDefinitionVersion(), instance.getTenantId()));
    }

    @Override
    public boolean isProcessDefinitionMultiTenantMode() {
        ConfigurationOption PprocessDefinitionMultiTenantMode = processEngineConfiguration.getOptionContainer()
                .get(ProcessDefinitionMultiTenantModeOption.PROCESS_DEFINITION_MULTI_TENANT_MODE_OPTION.getId());

        return PprocessDefinitionMultiTenantMode != null && PprocessDefinitionMultiTenantMode.isEnabled();
    }

    @Override
    public String buildProcessDefinitionUniqueKey(String processDefinitionId, String version, String tenantId) {
        if (isProcessDefinitionMultiTenantMode()) {
            if (StringUtil.isEmpty(tenantId)) {
                return IdAndVersionUtil.buildProcessDefinitionKey(processDefinitionId, version);
            }
            return IdAndVersionUtil.buildProcessDefinitionKey(processDefinitionId, version) + ":" + tenantId;
        }

        return IdAndVersionUtil.buildProcessDefinitionKey(processDefinitionId, version);
    }
}
