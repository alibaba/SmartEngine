package com.alibaba.smart.framework.engine.deployment;

import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface ProcessDefinitionContainer {

    /**
     * 安装流程
     *
     * @param pvmProcessDefinition 流程
     */
    void install(PvmProcessDefinition pvmProcessDefinition);

    /**
     * @param processDefinitionId
     * @param version
     */
    void uninstall(String processDefinitionId, String version);

    /**
     * 获取流程
     *
     * @param processId 流程ID
     * @param version   版本
     * @return 流程
     */
    PvmProcessDefinition get(String processId, String version);

    /**
     * 获取流程
     *
     * @param uri 流程URI
     * @return 流程
     */
    PvmProcessDefinition get(String uri);
}
