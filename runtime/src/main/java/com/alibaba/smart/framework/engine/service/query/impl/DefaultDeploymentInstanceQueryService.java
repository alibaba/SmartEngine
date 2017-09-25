package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.DeploymentInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.param.DeploymentInstanceParam;
import com.alibaba.smart.framework.engine.service.param.PaginateRequest;
import com.alibaba.smart.framework.engine.service.query.DeploymentInstanceQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  07:48.
 */
public class DefaultDeploymentInstanceQueryService implements DeploymentInstanceQueryService {

    private ExtensionPointRegistry extensionPointRegistry;


    public DefaultDeploymentInstanceQueryService(ExtensionPointRegistry extensionPointRegistry) {
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
    public List<DeploymentInstance> findDeploymentList(DeploymentInstanceParam deploymentInstanceParam) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        DeploymentInstanceStorage deploymentInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(DeploymentInstanceStorage.class);

        List<DeploymentInstance> deploymentInstanceList = deploymentInstanceStorage.findByPage(deploymentInstanceParam);
        return  deploymentInstanceList;
    }

    @Override
    public Integer queryDeploymentInstanceCount(DeploymentInstanceParam deploymentInstanceParam) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        DeploymentInstanceStorage deploymentInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(DeploymentInstanceStorage.class);

        int count = deploymentInstanceStorage.count(deploymentInstanceParam);
        return count;
    }

}
