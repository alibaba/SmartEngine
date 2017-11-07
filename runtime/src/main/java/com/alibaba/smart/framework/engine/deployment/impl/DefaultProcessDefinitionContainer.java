package com.alibaba.smart.framework.engine.deployment.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public class DefaultProcessDefinitionContainer implements ProcessDefinitionContainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultProcessDefinitionContainer.class);

    /**
     *   这个 CHM 可能会被更新, 在database 模式下会被远程通知到.
     */
    @Getter
    private Map<String, PvmProcessDefinition> pvmProcessDefinitionConcurrentHashMap = new ConcurrentHashMap<String, PvmProcessDefinition>();


    /**
     * 同上.
     */
    @Getter
    private Map<String, ProcessDefinition> processDefinitionConcurrentHashMap = new ConcurrentHashMap<String, ProcessDefinition>();


    @Override
    public void install(PvmProcessDefinition pvmProcessDefinition, ProcessDefinition processDefinition) {
        String processDefinitionId = pvmProcessDefinition.getId();
        String version = pvmProcessDefinition.getVersion();

        String uniqueKey = this.buildProcessDefinitionKey(processDefinitionId, version);
        this.installPvmProcessDefinition(uniqueKey, pvmProcessDefinition);
        this.installProcessDefinition(uniqueKey,processDefinition);

    }

    @Override
    public void uninstall(String processDefinitionId, String version) {
        String uniqueKey = this.buildProcessDefinitionKey(processDefinitionId, version);
        this.pvmProcessDefinitionConcurrentHashMap.remove(uniqueKey);
        this.processDefinitionConcurrentHashMap.remove(uniqueKey);

    }

    @Override
    public PvmProcessDefinition getPvmProcessDefinition(String processDefinitionId, String version) {
        String uri = this.buildProcessDefinitionKey(processDefinitionId, version);
        return this.getPvmProcessDefinition(uri);
    }


    @Override
    public PvmProcessDefinition getPvmProcessDefinition(String uri) {
        return this.pvmProcessDefinitionConcurrentHashMap.get(uri);
    }

    @Override
    public ProcessDefinition getProcessDefinition(String processDefinitionId, String version) {
        String uri = this.buildProcessDefinitionKey(processDefinitionId, version);
        return this.getProcessDefinition(uri);
    }

    @Override
    public ProcessDefinition getProcessDefinition(String uri) {
        return this.processDefinitionConcurrentHashMap.get(uri);
    }

    private void installPvmProcessDefinition(String uri, PvmProcessDefinition pvmProcessDefinition) {
        pvmProcessDefinition.setUri(uri);

        PvmProcessDefinition existedPvmProcessDefinition = pvmProcessDefinitionConcurrentHashMap.get(uri);
        if(null!= existedPvmProcessDefinition){
            LOGGER.warn(" Duplicated processDefinitionId and version found for unique key "+uri+" , but it's ok for deploy the process definition repeatedly. BUT this message should be NOTICED. ");
        }

        this.pvmProcessDefinitionConcurrentHashMap.put(uri, pvmProcessDefinition);
    }


    private void installProcessDefinition(String uri, ProcessDefinition processDefinition) {

        ProcessDefinition existedPvmProcessDefinition = processDefinitionConcurrentHashMap.get(uri);
        if(null!= existedPvmProcessDefinition){
            LOGGER.warn(" Duplicated processDefinitionId and version found for unique key "+uri+" , but it's ok for deploy the process definition repeatedly. BUT this message should be NOTICED. ");
        }

        this.processDefinitionConcurrentHashMap.put(uri, processDefinition);
    }

    private String buildProcessDefinitionKey(String processDefinitionId, String version) {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(processDefinitionId);
        uriBuilder.append(":").append(version);

        return uriBuilder.toString();
    }


}
