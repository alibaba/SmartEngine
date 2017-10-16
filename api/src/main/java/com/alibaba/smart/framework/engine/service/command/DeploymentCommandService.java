package com.alibaba.smart.framework.engine.service.command;

import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.service.param.command.CreateDeploymentCommand;
import com.alibaba.smart.framework.engine.service.param.command.UpdateDeploymentCommand;

/**
 * 将流程定义文件持久化到 数据库里面，并负责调用 RepositoryCommandService 完成解析。
 *
 * 目前是支持在 database 模式下，
 * Created by 高海军 帝奇 74394 on 2017 September  17:15.
 */
public interface DeploymentCommandService {

    DeploymentInstance createDeployment(CreateDeploymentCommand createDeploymentCommand) ;

    DeploymentInstance updateDeployment(UpdateDeploymentCommand updateDeploymentCommand) ;

    void inactivateDeploymentInstance(Long deploymentInstanceId);

    void activateDeploymentInstance(Long deploymentInstanceId);

    void deleteDeploymentInstanceLogically(Long deploymentInstanceId);

}
