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

    /**
     * 创建流程实例,解析流程定义并部署到本地内存中.
     *
     * @param createDeploymentCommand
     * @return
     */
    DeploymentInstance createDeployment(CreateDeploymentCommand createDeploymentCommand) ;

    /**
     * 更新部署实例,但是不涉及修改部署实例的部署状态(比如说 active,inactive)
     * 另外,在更新时, 如果部署实例的部署状态是 active, 则解析流程定义并部署到本地内存中.如果部署实例的部署状态是 inactive, 则<bold>不会解析流程定义<bold/>.
     * @param updateDeploymentCommand
     * @return
     */
    DeploymentInstance updateDeployment(UpdateDeploymentCommand updateDeploymentCommand) ;

    void inactivateDeploymentInstance(Long deploymentInstanceId);

    void activateDeploymentInstance(Long deploymentInstanceId);

    void deleteDeploymentInstanceLogically(Long deploymentInstanceId);

}
