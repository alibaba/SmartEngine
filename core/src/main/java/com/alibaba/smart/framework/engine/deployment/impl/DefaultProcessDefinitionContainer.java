package com.alibaba.smart.framework.engine.deployment.impl;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.tree.ActivityTreeNode;
import com.alibaba.smart.framework.engine.common.util.IdAndVersionUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
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


    // 添加活动树缓存，使用ConcurrentHashMap保证线程安全. 当流程定义更新后,需要 clear 这个 cache 即可
    public static Map<String, ActivityTreeNode> ACTIVITY_TREE_CACHE = new ConcurrentHashMap<>();

    // 添加缓存用于存储 calcCountOfTheJoinLatch 方法的计算结果
    public static final Map<String, Integer> JOIN_LATCH_COUNT_CACHE = new ConcurrentHashMap<>();

    /**
     * 同上.
     */
    @Getter
    private Map<String, ProcessDefinition> processDefinitionConcurrentHashMap = new ConcurrentHashMap<String, ProcessDefinition>();


    @Override
    public void install(PvmProcessDefinition pvmProcessDefinition, ProcessDefinition processDefinition) {
        String processDefinitionId = pvmProcessDefinition.getId();
        String version = pvmProcessDefinition.getVersion();
        String tenantId = pvmProcessDefinition.getTenantId();

        String uniqueKey = IdAndVersionUtil.buildProcessDefinitionUniqueKey(processDefinitionId, version,tenantId);

        this.installPvmProcessDefinition(uniqueKey, pvmProcessDefinition);
        this.installProcessDefinition(uniqueKey,processDefinition);

        ACTIVITY_TREE_CACHE.entrySet().removeIf(entry -> entry.getKey().startsWith(uniqueKey));
        JOIN_LATCH_COUNT_CACHE.entrySet().removeIf(entry -> entry.getKey().startsWith(uniqueKey));

    }

    @Override
    public void uninstall(String processDefinitionId, String version) {
        uninstall(processDefinitionId, version,null);
    }

    @Override
    public void uninstall(String processDefinitionId, String version,String tenantId) {
        String uniqueKey = IdAndVersionUtil.buildProcessDefinitionUniqueKey(processDefinitionId, version,tenantId);
        this.pvmProcessDefinitionConcurrentHashMap.remove(uniqueKey);
        this.processDefinitionConcurrentHashMap.remove(uniqueKey);

        ACTIVITY_TREE_CACHE.entrySet().removeIf(entry -> entry.getKey().startsWith(uniqueKey));
        JOIN_LATCH_COUNT_CACHE.entrySet().removeIf(entry -> entry.getKey().startsWith(uniqueKey));
    }

    @Override
    public PvmProcessDefinition getPvmProcessDefinition(String processDefinitionId, String version) {
        return this.getPvmProcessDefinition(processDefinitionId, version, null);
    }

    @Override
    public PvmProcessDefinition getPvmProcessDefinition(String processDefinitionId, String version,String tenantId) {
        String uniqueKey = IdAndVersionUtil.buildProcessDefinitionUniqueKey(processDefinitionId, version,tenantId);
        return this.getPvmProcessDefinition(uniqueKey);
    }


    @Override
    public PvmProcessDefinition getPvmProcessDefinition(String uniqueKey) {
        return this.pvmProcessDefinitionConcurrentHashMap.get(uniqueKey);
    }

    @Override
    public ProcessDefinition getProcessDefinition(String processDefinitionId, String version) {
        return this.getProcessDefinition(processDefinitionId, version,null);
    }

    @Override
    public ProcessDefinition getProcessDefinition(String processDefinitionId, String version,String tenantId) {
        String uniqueKey = IdAndVersionUtil.buildProcessDefinitionUniqueKey(processDefinitionId, version,tenantId);
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
