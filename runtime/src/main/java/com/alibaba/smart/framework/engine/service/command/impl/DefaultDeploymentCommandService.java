package com.alibaba.smart.framework.engine.service.command.impl;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.DeploymentStatusConstant;
import com.alibaba.smart.framework.engine.constant.LogicStatusConstant;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultDeploymentInstance;
import com.alibaba.smart.framework.engine.instance.storage.DeploymentInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.command.DeploymentCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.param.CreateDeploymentRequest;
import com.alibaba.smart.framework.engine.service.param.UpdateDeploymentRequest;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  07:47.
 */
public class DefaultDeploymentCommandService implements DeploymentCommandService, LifeCycleListener {

    @Override
    public DeploymentInstance createDeployment(CreateDeploymentRequest createDeploymentRequest) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        SmartEngine smartEngine = extensionPointRegistry.getExtensionPoint(SmartEngine.class);
        RepositoryCommandService repositoryCommandService =  smartEngine.getRepositoryCommandService();
        DeploymentInstanceStorage deploymentInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(DeploymentInstanceStorage.class);

        String  processDefinitionContent = createDeploymentRequest.getProcessDefinitionContent();

        //FIXME 明确下是否需要在这里部署.
        ProcessDefinition processDefinition =  repositoryCommandService.deployWithUTF8Content(processDefinitionContent);

        DeploymentInstance deploymentInstance  = new DefaultDeploymentInstance();

        deploymentInstance.setProcessDefinitionContent(processDefinitionContent);
        deploymentInstance.setProcessDefinitionId(processDefinition.getId());
        deploymentInstance.setProcessDefinitionVersion(processDefinition.getVersion());

        deploymentInstance.setProcessDefinitionName(createDeploymentRequest.getProcessDefinitionName());
        deploymentInstance.setProcessDefinitionDesc(createDeploymentRequest.getProcessDefinitionDesc());
        deploymentInstance.setProcessDefinitionType(createDeploymentRequest.getProcessDefinitionType());

        deploymentInstance.setDeploymentUserId(createDeploymentRequest.getDeploymentUserId());

        deploymentInstance.setDeploymentStatus(createDeploymentRequest.getDeploymentStatus());
        deploymentInstance.setLogicStatus(LogicStatusConstant.VALID);


        deploymentInstance = deploymentInstanceStorage.insert(deploymentInstance);

        return deploymentInstance;
    }

    @Override
    public DeploymentInstance updateDeployment(UpdateDeploymentRequest updateDeploymentRequest) {

        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        DeploymentInstanceStorage deploymentInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(DeploymentInstanceStorage.class);


        Long   deployInstanceId =  updateDeploymentRequest.getDeployInstanceId();
        DeploymentInstance currentDeploymentInstance = deploymentInstanceStorage.findById(deployInstanceId);

        if(null == currentDeploymentInstance){
            throw  new EngineException("Can't find a deploymentInstance by deployInstanceId: "+deployInstanceId);
        }

        //1. 新增一条,删除一个, version+1(TODO), 不能存在两个活跃的 processDefinitionId 和 Version
        //2. 但是万一 db 写失败,有可能导致 内存的数据被清空的情况.

        CreateDeploymentRequest createDeploymentRequest = new CreateDeploymentRequest();
        createDeploymentRequest.setProcessDefinitionType(updateDeploymentRequest.getProcessDefinitionType());
        createDeploymentRequest.setProcessDefinitionName(updateDeploymentRequest.getProcessDefinitionName());
        createDeploymentRequest.setProcessDefinitionDesc(updateDeploymentRequest.getProcessDefinitionDesc());
        createDeploymentRequest.setDeploymentStatus(updateDeploymentRequest.getDeploymentStatus());
        createDeploymentRequest.setProcessDefinitionContent(updateDeploymentRequest.getProcessDefinitionContent());

        DeploymentInstance newDeploymentInstance =  this.createDeployment(createDeploymentRequest);

        currentDeploymentInstance.setLogicStatus(LogicStatusConstant.DELETED);
        currentDeploymentInstance.setDeploymentStatus(DeploymentStatusConstant.INACTIVE);
        deploymentInstanceStorage.update(currentDeploymentInstance);


        //FIXME 根据更新条件,来删除内存的状态

        return newDeploymentInstance;
    }


    @Override
    public void inactivateDeploymentInstance(Long deploymentInstanceId) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        DeploymentInstanceStorage deploymentInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(DeploymentInstanceStorage.class);

        DeploymentInstance currentDeploymentInstance = deploymentInstanceStorage.findById(deploymentInstanceId);

        if(null == currentDeploymentInstance){
            throw  new EngineException("Can't find a deploymentInstance by deployInstanceId: "+deploymentInstanceId);
        }

        currentDeploymentInstance.setDeploymentStatus(DeploymentStatusConstant.INACTIVE);
        deploymentInstanceStorage.update(currentDeploymentInstance);

        processDefinitionContainer.uninstall(currentDeploymentInstance.getProcessDefinitionId(),currentDeploymentInstance.getProcessDefinitionVersion());
    }

    @Override
    public void activateDeploymentInstance(Long deploymentInstanceId) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        SmartEngine smartEngine = extensionPointRegistry.getExtensionPoint(SmartEngine.class);
        RepositoryCommandService repositoryCommandService =  smartEngine.getRepositoryCommandService();
        DeploymentInstanceStorage deploymentInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(DeploymentInstanceStorage.class);

        DeploymentInstance currentDeploymentInstance = deploymentInstanceStorage.findById(deploymentInstanceId);

        if(null == currentDeploymentInstance){
            throw  new EngineException("Can't find a deploymentInstance by deployInstanceId: "+deploymentInstanceId);
        }

        currentDeploymentInstance.setDeploymentStatus(DeploymentStatusConstant.ACTVIE);
        deploymentInstanceStorage.update(currentDeploymentInstance);

        repositoryCommandService.deployWithUTF8Content(currentDeploymentInstance.getProcessDefinitionContent());

    }

    @Override
    public void deleteDeploymentInstanceLogically(Long deploymentInstanceId) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        DeploymentInstanceStorage deploymentInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(DeploymentInstanceStorage.class);

        DeploymentInstance currentDeploymentInstance = deploymentInstanceStorage.findById(deploymentInstanceId);

        if(null == currentDeploymentInstance){
            throw  new EngineException("Can't find a deploymentInstance by deployInstanceId: "+deploymentInstanceId);
        }

        currentDeploymentInstance.setLogicStatus(LogicStatusConstant.DELETED);
        currentDeploymentInstance.setDeploymentStatus(DeploymentStatusConstant.INACTIVE);
        deploymentInstanceStorage.update(currentDeploymentInstance);

        processDefinitionContainer.uninstall(currentDeploymentInstance.getProcessDefinitionId(),currentDeploymentInstance.getProcessDefinitionVersion());

    }


    private ExtensionPointRegistry extensionPointRegistry;


    public DefaultDeploymentCommandService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }


    private ProcessDefinitionContainer processDefinitionContainer;


    @Override
    public void start() {
        this.processDefinitionContainer = this.extensionPointRegistry.getExtensionPoint(ProcessDefinitionContainer.class);
    }

    @Override
    public void stop() {

    }
}
