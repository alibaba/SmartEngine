package com.alibaba.smart.framework.engine.persister.database.service;

import com.alibaba.smart.framework.engine.instance.storage.DeploymentInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.persister.database.entity.DeploymentInstanceEntity;

/**
 * Created by yueyu.yr on 2017/9/22.
 *
 * @author yueyu.yr
 * @date 2017/09/22
 */
public class RelationshipDatabaseDeploymentInstanceStorage implements DeploymentInstanceStorage {
    @Override
    public DeploymentInstance insert(DeploymentInstance deploymentInstance) {
        return null;
    }

    @Override
    public DeploymentInstance update(DeploymentInstance deploymentInstance) {
        return null;
    }

    @Override
    public DeploymentInstance find(DeploymentInstance deploymentInstance) {
        return null;
    }

    @Override
    public DeploymentInstance findByPage(DeploymentInstance deploymentInstance, int pageOffSide, int pageSize) {
        return null;
    }

    @Override
    public int count(DeploymentInstance deploymentInstance) {
        return 0;
    }

    @Override
    public void remove(Long id) {

    }

    private DeploymentInstanceEntity convertByDeploymentInstance(DeploymentInstance deploymentInstance) {
        DeploymentInstanceEntity deploymentInstanceEntity = new DeploymentInstanceEntity();
        deploymentInstanceEntity.setLogicStatus(deploymentInstance.getLogicStatus());
        deploymentInstanceEntity.setDeploymentStatus(deploymentInstance.getDeploymentStatus());
        deploymentInstanceEntity.setDeploymentUserId(deploymentInstance.getDeploymentUserId());
        deploymentInstanceEntity.setProcessDefinitionContent(deploymentInstance.getProcessDefinitionContent());
        deploymentInstanceEntity.setProcessDefinitionDesc(deploymentInstance.getProcessDefinitionDesc());
        deploymentInstanceEntity.setProcessDefinitionName(deploymentInstanceEntity.getProcessDefinitionName());
        deploymentInstanceEntity.setProcessDefinitionType(deploymentInstanceEntity.getProcessDefinitionType());
        deploymentInstanceEntity.setProcessDefinitionVersion(deploymentInstance.getProcessDefinitionVersion());
        return deploymentInstanceEntity;
    }
}
