package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.List;

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
    public DeploymentInstance findOne(Long deploymentInstanceId) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        DeploymentInstanceStorage deploymentInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(DeploymentInstanceStorage.class);

        DeploymentInstance currentDeploymentInstance = deploymentInstanceStorage.findById(deploymentInstanceId);
        return  currentDeploymentInstance;
    }

    @Override
    public List<DeploymentInstance> findDeploymentList(DeploymentInstanceQueryParam deploymentInstanceQueryParam) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        DeploymentInstanceStorage deploymentInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(DeploymentInstanceStorage.class);

        List<DeploymentInstance> deploymentInstanceList = deploymentInstanceStorage.findByPage(
            deploymentInstanceQueryParam);
        return  deploymentInstanceList;
    }

    @Override
    public Integer queryDeploymentInstanceCount(DeploymentInstanceQueryParam deploymentInstanceQueryParam) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        DeploymentInstanceStorage deploymentInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(DeploymentInstanceStorage.class);

        int count = deploymentInstanceStorage.count(deploymentInstanceQueryParam);
        return count;
    }

}
