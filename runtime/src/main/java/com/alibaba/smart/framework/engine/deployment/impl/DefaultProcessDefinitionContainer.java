package com.alibaba.smart.framework.engine.deployment.impl;

import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public class DefaultProcessDefinitionContainer implements ProcessDefinitionContainer {


    private Map<String, PvmProcessDefinition> pvmProcessDefinitionConcurrentHashMap = new ConcurrentHashMap<String, PvmProcessDefinition>();

    @Override
    public void install(PvmProcessDefinition pvmProcessDefinition) {
        String processDefinitionId = pvmProcessDefinition.getId();
        String version = pvmProcessDefinition.getVersion();

        String uniqueKey = this.buildProcessDefinitionKey(processDefinitionId, version);
        this.install(uniqueKey, pvmProcessDefinition);

    }

    @Override
    public PvmProcessDefinition get(String processDefinitionId, String version) {
        String uri = this.buildProcessDefinitionKey(processDefinitionId, version);
        return this.get(uri);
    }


    @Override
    public PvmProcessDefinition get(String uri) {
        return this.pvmProcessDefinitionConcurrentHashMap.get(uri);
    }


    private void install(String uri, PvmProcessDefinition runtimeProcess) {
        runtimeProcess.setUri(uri);
        this.pvmProcessDefinitionConcurrentHashMap.put(uri, runtimeProcess);
    }

    private String buildProcessDefinitionKey(String processDefinitionId, String version) {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(processDefinitionId);
        uriBuilder.append(":").append(version);

        return uriBuilder.toString();
    }


}
