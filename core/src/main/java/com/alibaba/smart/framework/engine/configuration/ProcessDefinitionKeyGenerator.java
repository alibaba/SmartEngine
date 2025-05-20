package com.alibaba.smart.framework.engine.configuration;


import com.alibaba.smart.framework.engine.model.instance.Instance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * 流程定义唯一识别码生成器
 *
 * @author yanricheng
 * @date 2025/5/20
 */
public interface ProcessDefinitionKeyGenerator {


    void generate(ProcessInstance instance);

    /**
     * 流程定义是否多租户模式
     *  @see #com.alibaba.smart.framework.engine.configuration.impl.option.ProcessDefinitionMultiTenantModeOption
     * @return
     */
    boolean isProcessDefinitionMultiTenantMode();

    /**
     * 流程定义唯一识别码,所有生成key地方都都聚拢这个方法<br>
     * 主要应用场景：<br>
     * @see #com.alibaba.smart.framework.engine.configuration.impl.option.ProcessDefinitionMultiTenantModeOption
     * @param processDefinitionId 流程图定义id
     * @param version 流程图定义版本
     * @param tenantId 租户id
     * @return
     * 1. 流程定义和流程实例数据都是多租户模式：每个企业都有自己的业务流程定义时返回： ${id}:${version}:${tenantId}<br>
     * 2. 流程定义是全局的，但是，流程实例是多租户模式时返回：${id}:${version}<br>
     */
    String buildProcessDefinitionUniqueKey(String processDefinitionId, String version, String tenantId);
}
