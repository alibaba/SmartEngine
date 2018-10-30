package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.DeploymentInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.param.query.DeploymentInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.query.DeploymentQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  07:48.
 */
public class DefaultDeploymentQueryService implements DeploymentQueryService {

    private ExtensionPointRegistry extensionPointRegistry;


    public DefaultDeploymentQueryService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public DeploymentInstance findById(String deploymentInstanceId) {
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(
            SmartEngine.class).getProcessEngineConfiguration();
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        DeploymentInstanceStorage deploymentInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(DeploymentInstanceStorage.class);

        DeploymentInstance currentDeploymentInstance = deploymentInstanceStorage.findById(deploymentInstanceId,
            processEngineConfiguration);
        return  currentDeploymentInstance;
    }

    @Override
    public List<DeploymentInstance> findList(DeploymentInstanceQueryParam deploymentInstanceQueryParam) {
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();

        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        DeploymentInstanceStorage deploymentInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(DeploymentInstanceStorage.class);

        List<DeploymentInstance> deploymentInstanceList = deploymentInstanceStorage.findByPage(
            deploymentInstanceQueryParam, processEngineConfiguration);
        return  deploymentInstanceList;
    }

    @Override
    public Integer count(DeploymentInstanceQueryParam deploymentInstanceQueryParam) {
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();

        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        DeploymentInstanceStorage deploymentInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(DeploymentInstanceStorage.class);

        int count = deploymentInstanceStorage.count(deploymentInstanceQueryParam, processEngineConfiguration);
        return count;
    }

}
