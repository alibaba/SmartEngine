package com.alibaba.smart.framework.engine.service.command;

import java.io.InputStream;

import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.service.param.CreateDeploymentRequest;
import com.alibaba.smart.framework.engine.service.param.UpdateDeploymentRequest;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  17:15.
 */
public interface DeploymentInstanceCommandService {


    DeploymentInstance createDeployment(CreateDeploymentRequest createDeploymentRequest) ;

    DeploymentInstance createDeployment(UpdateDeploymentRequest updateDeploymentRequest) ;


    void inactivateDeploymentInstance(Long deploymentInstanceId);

    void activateDeploymentInstance(Long deploymentInstanceId);

    void deleteDeploymentInstance(Long deploymentId);

}
