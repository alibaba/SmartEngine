package com.alibaba.smart.framework.engine.deployment;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;

import java.util.Map;

/**
 * @author 高海军 帝奇 2016.11.11
 * @author ettear 2016.04.13
 */
public interface ProcessDefinitionContainer {

    Map<String, PvmProcessDefinition> getPvmProcessDefinitionConcurrentHashMap();

    Map<String, ProcessDefinition> getProcessDefinitionConcurrentHashMap();

    /**
     * 安装流程
     *
     * @param pvmProcessDefinition 流程
     * @param processDefinition
     */
    void install(PvmProcessDefinition pvmProcessDefinition, ProcessDefinition processDefinition);

    /**
     * @param processDefinitionId
     * @param version
     */
    void uninstall(String processDefinitionId, String version);

    void uninstall(String processDefinitionId, String version, String tenantId);

    /**
     * 获取流程
     *
     * @param processDefinitionId 流程ID
     * @param version 版本
     * @return 流程
     */
    PvmProcessDefinition getPvmProcessDefinition(String processDefinitionId, String version);

    PvmProcessDefinition getPvmProcessDefinition(
            String processDefinitionId, String version, String tenantId);

    /**
     * 获取流程
     *
     * @param uri 流程URI
     * @return 流程
     */
    PvmProcessDefinition getPvmProcessDefinition(String uri);

    ProcessDefinition getProcessDefinition(String processDefinitionId, String version);

    ProcessDefinition getProcessDefinition(
            String processDefinitionId, String version, String tenantId);

    ProcessDefinition getProcessDefinition(String uri);
}
