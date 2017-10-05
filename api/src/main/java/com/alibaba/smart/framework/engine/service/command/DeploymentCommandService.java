package com.alibaba.smart.framework.engine.service.command;

import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.service.param.CreateDeploymentRequest;
import com.alibaba.smart.framework.engine.service.param.UpdateDeploymentRequest;

/**
 * 将流程定义文件持久化到 数据库里面，并负责调用 RepositoryCommandService 完成解析。
 *
 * 目前是支持在 database 模式下，
 * Created by 高海军 帝奇 74394 on 2017 September  17:15.
 */
public interface DeploymentCommandService {


    DeploymentInstance createDeployment(CreateDeploymentRequest createDeploymentRequest) ;

    DeploymentInstance updateDeployment(UpdateDeploymentRequest updateDeploymentRequest) ;

    void inactivateDeploymentInstance(Long deploymentInstanceId);

    void activateDeploymentInstance(Long deploymentInstanceId);

    void deleteDeploymentInstanceLogically(Long deploymentInstanceId);

}
