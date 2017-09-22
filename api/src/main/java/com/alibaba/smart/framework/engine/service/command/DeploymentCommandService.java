package com.alibaba.smart.framework.engine.service.command;

import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.service.param.CreateDeploymentRequest;
import com.alibaba.smart.framework.engine.service.param.UpdateDeploymentRequest;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  17:15.
 */
public interface DeploymentCommandService {


    DeploymentInstance createDeployment(CreateDeploymentRequest createDeploymentRequest) ;

    DeploymentInstance updateDeployment(UpdateDeploymentRequest updateDeploymentRequest) ;

    void inactivateDeploymentInstance(Long deploymentInstanceId);

    void activateDeploymentInstance(Long deploymentInstanceId);

    void deleteDeploymentInstanceLogically(Long deploymentInstanceId);

}
