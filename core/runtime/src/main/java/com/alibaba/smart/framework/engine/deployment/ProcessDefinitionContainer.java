package com.alibaba.smart.framework.engine.deployment;

import com.alibaba.smart.framework.engine.pvm.PvmProcessComponent;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;

/**
 * 流程容器 Created by ettear on 16-4-19.
 */
public interface ProcessDefinitionContainer {

    /**
     * 安装流程
     *
     * @param processComponent 流程
     */
    void install(PvmProcessComponent processComponent);

    /**
     * 获取流程
     *
     * @param processId 流程ID
     * @param version 版本
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
