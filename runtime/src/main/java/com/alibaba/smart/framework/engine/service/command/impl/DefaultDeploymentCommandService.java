package com.alibaba.smart.framework.engine.service.command.impl;

import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.service.command.DeploymentCommandService;
import com.alibaba.smart.framework.engine.service.param.CreateDeploymentRequest;
import com.alibaba.smart.framework.engine.service.param.UpdateDeploymentRequest;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  07:47.
 */
public class DefaultDeploymentCommandService implements DeploymentCommandService {
    @Override
    public DeploymentInstance createDeployment(CreateDeploymentRequest createDeploymentRequest) {
        return null;
    }

    @Override
    public DeploymentInstance updateDeployment(UpdateDeploymentRequest updateDeploymentRequest) {
        return null;
    }

    @Override
    public void inactivateDeploymentInstance(Long deploymentInstanceId) {

    }

    @Override
    public void activateDeploymentInstance(Long deploymentInstanceId) {

    }

    @Override
    public void deleteDeploymentInstanceLogically(Long deploymentInstanceId) {

    }
}
