package com.alibaba.smart.framework.engine.deployment.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.smart.framework.engine.common.util.IdAndVersionBuilder;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = ProcessDefinitionContainer.class)
public class DefaultProcessDefinitionContainer implements ProcessDefinitionContainer, ProcessEngineConfigurationAware {

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

        String uniqueKey = IdAndVersionBuilder.buildProcessDefinitionKey(processDefinitionId, version);

        this.installPvmProcessDefinition(uniqueKey, pvmProcessDefinition);
        this.installProcessDefinition(uniqueKey,processDefinition);

    }

    @Override
    public void uninstall(String processDefinitionId, String version) {
        String uniqueKey = IdAndVersionBuilder.buildProcessDefinitionKey(processDefinitionId, version);
        this.pvmProcessDefinitionConcurrentHashMap.remove(uniqueKey);
        this.processDefinitionConcurrentHashMap.remove(uniqueKey);

    }

    @Override
    public PvmProcessDefinition getPvmProcessDefinition(String processDefinitionId, String version) {
        String uniqueKey = IdAndVersionBuilder.buildProcessDefinitionKey(processDefinitionId, version);
        return this.getPvmProcessDefinition(uniqueKey);
    }


    @Override
    public PvmProcessDefinition getPvmProcessDefinition(String uniqueKey) {
        return this.pvmProcessDefinitionConcurrentHashMap.get(uniqueKey);
    }

    @Override
    public ProcessDefinition getProcessDefinition(String processDefinitionId, String version) {
        String uniqueKey = IdAndVersionBuilder.buildProcessDefinitionKey(processDefinitionId, version);
        return this.getProcessDefinition(uniqueKey);
    }

    @Override
    public ProcessDefinition getProcessDefinition(String uniqueKey) {
        return this.processDefinitionConcurrentHashMap.get(uniqueKey);
    }

    private void installPvmProcessDefinition(String uniqueKey, PvmProcessDefinition pvmProcessDefinition) {
        pvmProcessDefinition.setIdAndVersion(uniqueKey);

        PvmProcessDefinition existedPvmProcessDefinition = pvmProcessDefinitionConcurrentHashMap.get(uniqueKey);
        if(null!= existedPvmProcessDefinition){
            LOGGER.warn(" Duplicated processDefinitionId and version found for unique key "+uniqueKey+" , but it's ok for deploy the process definition repeatedly. BUT this message should be NOTICED. ");
        }

        this.pvmProcessDefinitionConcurrentHashMap.put(uniqueKey, pvmProcessDefinition);
    }


    private void installProcessDefinition(String uniqueKey, ProcessDefinition processDefinition) {

        ProcessDefinition existedPvmProcessDefinition = processDefinitionConcurrentHashMap.get(uniqueKey);
        if(null!= existedPvmProcessDefinition){
            LOGGER.warn(" Duplicated processDefinitionId and version found for unique key "+uniqueKey+" , but it's ok for deploy the process definition repeatedly. BUT this message should be NOTICED. ");
        }

        this.processDefinitionConcurrentHashMap.put(uniqueKey, processDefinition);
    }

    private ProcessEngineConfiguration processEngineConfiguration;

    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }


}
