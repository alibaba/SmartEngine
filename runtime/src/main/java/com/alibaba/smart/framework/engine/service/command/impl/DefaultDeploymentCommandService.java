package com.alibaba.smart.framework.engine.service.command.impl;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.DeploymentStatusConstant;
import com.alibaba.smart.framework.engine.constant.LogicStatusConstant;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.impl.DefaultDeploymentInstance;
import com.alibaba.smart.framework.engine.instance.storage.DeploymentInstanceStorage;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.command.DeploymentCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.param.command.CreateDeploymentCommand;
import com.alibaba.smart.framework.engine.service.param.command.UpdateDeploymentCommand;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  07:47.
 */
public class DefaultDeploymentCommandService implements DeploymentCommandService, LifeCycleHook {

    @Override
    public DeploymentInstance createDeployment(CreateDeploymentCommand createDeploymentCommand) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        SmartEngine smartEngine = extensionPointRegistry.getExtensionPoint(SmartEngine.class);
        RepositoryCommandService repositoryCommandService =  smartEngine.getRepositoryCommandService();
        DeploymentInstanceStorage deploymentInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(DeploymentInstanceStorage.class);

        String  processDefinitionContent = createDeploymentCommand.getProcessDefinitionContent();

        ProcessDefinition processDefinition =  repositoryCommandService.deployWithUTF8Content(processDefinitionContent).getFirstProcessDefinition();

        DeploymentInstance deploymentInstance  = new DefaultDeploymentInstance();

        String id = smartEngine.getProcessEngineConfiguration().getIdGenerator().getId();
        deploymentInstance.setInstanceId(id);
        deploymentInstance.setProcessDefinitionContent(processDefinitionContent);
        deploymentInstance.setProcessDefinitionId(processDefinition.getId());
        deploymentInstance.setProcessDefinitionVersion(processDefinition.getVersion());

        deploymentInstance.setProcessDefinitionName(createDeploymentCommand.getProcessDefinitionName());
        deploymentInstance.setProcessDefinitionDesc(createDeploymentCommand.getProcessDefinitionDesc());
        deploymentInstance.setProcessDefinitionType(createDeploymentCommand.getProcessDefinitionType());
        deploymentInstance.setProcessDefinitionCode(createDeploymentCommand.getProcessDefinitionCode());

        deploymentInstance.setDeploymentUserId(createDeploymentCommand.getDeploymentUserId());

        deploymentInstance.setDeploymentStatus(createDeploymentCommand.getDeploymentStatus());
        deploymentInstance.setLogicStatus(LogicStatusConstant.VALID);

        deploymentInstance = deploymentInstanceStorage.insert(deploymentInstance, smartEngine.getProcessEngineConfiguration());

        return deploymentInstance;
    }

    @Override
    public DeploymentInstance updateDeployment(UpdateDeploymentCommand updateDeploymentCommand) {
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();

        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        DeploymentInstanceStorage deploymentInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(DeploymentInstanceStorage.class);


        String   deployInstanceId =  updateDeploymentCommand.getDeployInstanceId();
        DeploymentInstance currentDeploymentInstance = deploymentInstanceStorage.findById(deployInstanceId,
            processEngineConfiguration);

        setUpdateValue(currentDeploymentInstance,updateDeploymentCommand);

        if(null == currentDeploymentInstance){
            throw  new EngineException("Can't find a deploymentInstance by deployInstanceId: "+deployInstanceId);
        }

        DeploymentInstance deploymentInstance =  deploymentInstanceStorage.update(currentDeploymentInstance,
            processEngineConfiguration);

        if(DeploymentStatusConstant.ACTIVE.equals(deploymentInstance.getDeploymentStatus())){
            String processDefinitionContent = updateDeploymentCommand.getProcessDefinitionContent();
            if(StringUtil.isNotEmpty(processDefinitionContent)){
                SmartEngine smartEngine = extensionPointRegistry.getExtensionPoint(SmartEngine.class);
                RepositoryCommandService repositoryCommandService =  smartEngine.getRepositoryCommandService();
                repositoryCommandService.deployWithUTF8Content(processDefinitionContent);

            }
        }

        return deploymentInstance;
    }

    private void setUpdateValue(DeploymentInstance currentDeploymentInstance,
                                UpdateDeploymentCommand updateDeploymentCommand) {
        if (updateDeploymentCommand.getProcessDefinitionContent() != null) {
            currentDeploymentInstance.setProcessDefinitionContent(updateDeploymentCommand.getProcessDefinitionContent());
        }
        if (updateDeploymentCommand.getProcessDefinitionDesc() != null) {
            currentDeploymentInstance.setProcessDefinitionDesc(updateDeploymentCommand.getProcessDefinitionDesc());
        }
        if (updateDeploymentCommand.getDeploymentUserId() != null) {
            currentDeploymentInstance.setDeploymentUserId(updateDeploymentCommand.getDeploymentUserId());
        }
        if (updateDeploymentCommand.getProcessDefinitionName() != null) {
            currentDeploymentInstance.setProcessDefinitionName(updateDeploymentCommand.getProcessDefinitionName());
        }
        if (updateDeploymentCommand.getProcessDefinitionType() != null) {
            currentDeploymentInstance.setProcessDefinitionType(updateDeploymentCommand.getProcessDefinitionType());
        }
    }

    @Override
    public void inactivateDeploymentInstance(String deploymentInstanceId) {
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        DeploymentInstanceStorage deploymentInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(DeploymentInstanceStorage.class);

        DeploymentInstance currentDeploymentInstance = deploymentInstanceStorage.findById(deploymentInstanceId,
            processEngineConfiguration);

        if(null == currentDeploymentInstance){
            throw  new EngineException("Can't find a deploymentInstance by deployInstanceId: "+deploymentInstanceId);
        }

        currentDeploymentInstance.setDeploymentStatus(DeploymentStatusConstant.INACTIVE);
        deploymentInstanceStorage.update(currentDeploymentInstance, processEngineConfiguration);

        //processDefinitionContainer.uninstall(currentDeploymentInstance.getProcessDefinitionId(),currentDeploymentInstance.getProcessDefinitionVersion());
    }

    @Override
    public void activateDeploymentInstance(String deploymentInstanceId) {
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        SmartEngine smartEngine = extensionPointRegistry.getExtensionPoint(SmartEngine.class);
        RepositoryCommandService repositoryCommandService =  smartEngine.getRepositoryCommandService();
        DeploymentInstanceStorage deploymentInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(DeploymentInstanceStorage.class);

        DeploymentInstance currentDeploymentInstance = deploymentInstanceStorage.findById(deploymentInstanceId,
            processEngineConfiguration);

        if(null == currentDeploymentInstance){
            throw  new EngineException("Can't find a deploymentInstance by deployInstanceId: "+deploymentInstanceId);
        }

        currentDeploymentInstance.setDeploymentStatus(DeploymentStatusConstant.ACTIVE);
        deploymentInstanceStorage.update(currentDeploymentInstance, processEngineConfiguration);

        repositoryCommandService.deployWithUTF8Content(currentDeploymentInstance.getProcessDefinitionContent());

    }

    @Override
    public void deleteDeploymentInstanceLogically(String deploymentInstanceId) {
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        DeploymentInstanceStorage deploymentInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(DeploymentInstanceStorage.class);

        DeploymentInstance currentDeploymentInstance = deploymentInstanceStorage.findById(deploymentInstanceId,
            processEngineConfiguration);

        if(null == currentDeploymentInstance){
            throw  new EngineException("Can't find a deploymentInstance by deployInstanceId: "+deploymentInstanceId);
        }

        currentDeploymentInstance.setLogicStatus(LogicStatusConstant.DELETED);
        currentDeploymentInstance.setDeploymentStatus(DeploymentStatusConstant.DELETED);
        deploymentInstanceStorage.update(currentDeploymentInstance, processEngineConfiguration);

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
