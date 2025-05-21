package com.alibaba.smart.framework.engine.configuration.impl.option;

import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;

/**
 * 流程定义是否是多租户模式：默认是多租户模式<br>
 * 主要应用场景：<br>
 * 1. 流程定义和流程实例数据都是多租户模式：每个企业都有自己的业务流程定义${id}:${version}:${tenantId}<br>
 * 2. 流程定义是全局的，但是，流程实例是多租户模式 ，流程定义的key：${id}:${version}<br>
 * @author yanricheng
 * @date 2025/5/20
 */
public class ProcessDefinitionMultiTenantShareOption implements ConfigurationOption {
    boolean isEnabled = true;
    public ProcessDefinitionMultiTenantShareOption(){
        this.isEnabled = true;
    }

    public ProcessDefinitionMultiTenantShareOption(boolean isEnabled){
        this.isEnabled = isEnabled;
    }
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public String getId() {
        return "processDefinitionShareOption";
    }

    @Override
    public Object getData() {
        return null;
    }
}
