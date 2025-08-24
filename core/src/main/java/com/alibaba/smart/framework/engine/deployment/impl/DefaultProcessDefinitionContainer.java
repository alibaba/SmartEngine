package com.alibaba.smart.framework.engine.deployment.impl;

import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.tree.ActivityTreeNode;
import com.alibaba.smart.framework.engine.common.util.IdAndVersionUtil;
import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.configuration.impl.option.ProcessDefinitionMultiTenantShareOption;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = ProcessDefinitionContainer.class)
public class DefaultProcessDefinitionContainer implements ProcessDefinitionContainer, ProcessEngineConfigurationAware {

    // 添加缓存用于存储 calcCountOfTheJoinLatch 方法的计算结果
    public static final Map<String, Integer> JOIN_LATCH_COUNT_CACHE = new ConcurrentHashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultProcessDefinitionContainer.class);
    // 添加活动树缓存，使用ConcurrentHashMap保证线程安全. 当流程定义更新后,需要 clear 这个 cache 即可
    public static Map<String, ActivityTreeNode> ACTIVITY_TREE_CACHE = new ConcurrentHashMap<>();
    /**
     * 这个 CHM 可能会被更新, 在database 模式下会被远程通知到.
     */
    @Getter
    private final Map<String, PvmProcessDefinition> pvmProcessDefinitionConcurrentHashMap = new ConcurrentHashMap<String, PvmProcessDefinition>();
    /**
     * 同上.
     */
    @Getter
    private final Map<String, ProcessDefinition> processDefinitionConcurrentHashMap = new ConcurrentHashMap<String, ProcessDefinition>();
    private ProcessEngineConfiguration processEngineConfiguration;



    @Override
    public void install(PvmProcessDefinition pvmProcessDefinition, ProcessDefinition processDefinition) {
        String processDefinitionId = pvmProcessDefinition.getId();
        String version = pvmProcessDefinition.getVersion();
        String tenantId = pvmProcessDefinition.getTenantId();

        String uniqueKey = IdAndVersionUtil.buildProcessDefinitionUniqueKey(processDefinitionId, version, tenantId);

        this.installPvmProcessDefinition(uniqueKey, pvmProcessDefinition);
        this.installProcessDefinition(uniqueKey, processDefinition);

        ACTIVITY_TREE_CACHE.entrySet().removeIf(entry -> entry.getKey().startsWith(uniqueKey));
        JOIN_LATCH_COUNT_CACHE.entrySet().removeIf(entry -> entry.getKey().startsWith(uniqueKey));

    }

    @Override
    public void uninstall(String processDefinitionId, String version) {
        String uniqueKey = IdAndVersionUtil.buildProcessDefinitionUniqueKey(processDefinitionId, version, null);
        uninstallProcessDefinition(uniqueKey);
    }

    @Override
    public void uninstall(String processDefinitionId, String version, String tenantId) {
        if (StringUtil.isEmpty(tenantId)) {
            throw new EngineException("tenantId can't be null,when uninstall tenant's process definition");
        }

        String uniqueKey = IdAndVersionUtil.buildProcessDefinitionUniqueKey(processDefinitionId, version, tenantId);
        uninstallProcessDefinition(uniqueKey);
    }

    private void uninstallProcessDefinition(String uniqueKey) {
        this.pvmProcessDefinitionConcurrentHashMap.remove(uniqueKey);
        this.processDefinitionConcurrentHashMap.remove(uniqueKey);

        ACTIVITY_TREE_CACHE.entrySet().removeIf(entry -> entry.getKey().startsWith(uniqueKey));
        JOIN_LATCH_COUNT_CACHE.entrySet().removeIf(entry -> entry.getKey().startsWith(uniqueKey));
    }

    @Override
    public PvmProcessDefinition getPvmProcessDefinition(String processDefinitionId, String version) {
        return this.getPvmProcessDefinition(IdAndVersionUtil.buildProcessDefinitionKey(processDefinitionId, version));
    }

    @Override
    public PvmProcessDefinition getPvmProcessDefinition(String processDefinitionId, String version, String tenantId) {
        if (StringUtil.isEmpty(tenantId) && !isProcessDefinitionShareMode()) {
            return null;
        }

        //通过${processDefinitionId}:${version}:${tenantId}获取
        String uniqueKey = IdAndVersionUtil.buildProcessDefinitionUniqueKey(processDefinitionId, version, tenantId);
        PvmProcessDefinition pvmProcessDefinition = this.getPvmProcessDefinition(uniqueKey);

        if (null != pvmProcessDefinition) {
            return pvmProcessDefinition;
        }


        //如果开启流程共享模式，则根据${processDefinitionId}:${version}获取
        if (null == pvmProcessDefinition && isProcessDefinitionShareMode()) {
            uniqueKey = IdAndVersionUtil.buildProcessDefinitionKey(processDefinitionId, version);
            pvmProcessDefinition = this.getPvmProcessDefinition(uniqueKey);
        }
        return pvmProcessDefinition;
    }

    public boolean isProcessDefinitionShareMode() {
        ConfigurationOption processDefinitionShareOption = processEngineConfiguration.getOptionContainer()
                .get(ProcessDefinitionMultiTenantShareOption.PROCESS_DEFINITION_MULTI_TENANT_SHARE_OPTION.getId());

        return processDefinitionShareOption != null && processDefinitionShareOption.isEnabled();
    }

    @Override
    public PvmProcessDefinition getPvmProcessDefinition(String uniqueKey) {
        return this.pvmProcessDefinitionConcurrentHashMap.get(uniqueKey);
    }

    @Override
    public ProcessDefinition getProcessDefinition(String processDefinitionId, String version) {
        return this.processDefinitionConcurrentHashMap.get(IdAndVersionUtil.buildProcessDefinitionKey(processDefinitionId, version));
    }

    @Override
    public ProcessDefinition getProcessDefinition(String processDefinitionId, String version, String tenantId) {
        if (StringUtil.isEmpty(tenantId) && !isProcessDefinitionShareMode()) {
            throw new EngineException("tenantId cannot be null unless process definition multi-tenant share mode is enabled. Please provide a valid tenantId or enable ProcessDefinitionMultiTenantShareOption.");
        }

        //通过${processDefinitionId}:${version}:${tenantId}获取
        String uniqueKey = IdAndVersionUtil.buildProcessDefinitionUniqueKey(processDefinitionId, version, tenantId);
        ProcessDefinition processDefinition = this.getProcessDefinition(uniqueKey);
        if (null != processDefinition) {
            return processDefinition;
        }

        //如果开启流程共享模式，则根据${processDefinitionId}:${version}获取
        if (null == processDefinition && isProcessDefinitionShareMode()) {
            uniqueKey = IdAndVersionUtil.buildProcessDefinitionKey(processDefinitionId, version);
            processDefinition = this.getProcessDefinition(uniqueKey);
        }

        return processDefinition;
    }

    @Override
    public ProcessDefinition getProcessDefinition(String uniqueKey) {
        return this.processDefinitionConcurrentHashMap.get(uniqueKey);
    }

    private void installPvmProcessDefinition(String uniqueKey, PvmProcessDefinition pvmProcessDefinition) {
        pvmProcessDefinition.setIdAndVersion(uniqueKey);

        PvmProcessDefinition existedPvmProcessDefinition = pvmProcessDefinitionConcurrentHashMap.get(uniqueKey);
        if (null != existedPvmProcessDefinition) {
            LOGGER.warn(" Duplicated processDefinitionId and version found for unique key " + uniqueKey + " , but it's ok for deploy the process definition repeatedly. BUT this message should be NOTICED. ");
        }

        this.pvmProcessDefinitionConcurrentHashMap.put(uniqueKey, pvmProcessDefinition);
    }

    private void installProcessDefinition(String uniqueKey, ProcessDefinition processDefinition) {

        ProcessDefinition existedPvmProcessDefinition = processDefinitionConcurrentHashMap.get(uniqueKey);
        if (null != existedPvmProcessDefinition) {
            LOGGER.warn(" Duplicated processDefinitionId and version found for unique key " + uniqueKey + " , but it's ok for deploy the process definition repeatedly. BUT this message should be NOTICED. ");
        }

        this.processDefinitionConcurrentHashMap.put(uniqueKey, processDefinition);
    }

    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }


}
