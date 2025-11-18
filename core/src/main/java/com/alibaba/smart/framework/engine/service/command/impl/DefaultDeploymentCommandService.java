package com.alibaba.smart.framework.engine.service.command.impl;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.constant.DeploymentStatusConstant;
import com.alibaba.smart.framework.engine.constant.LogicStatusConstant;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.ecology.designer.converter.JsonToBpmnConverter;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.impl.DefaultDeploymentInstance;
import com.alibaba.smart.framework.engine.instance.storage.DeploymentInstanceStorage;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.service.command.DeploymentCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.param.command.CreateDeploymentCommand;
import com.alibaba.smart.framework.engine.service.param.command.UpdateDeploymentCommand;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  07:47.
 */
@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = DeploymentCommandService.class)

public class DefaultDeploymentCommandService implements DeploymentCommandService, LifeCycleHook,
    ProcessEngineConfigurationAware {

    @Override
    public DeploymentInstance createDeployment(CreateDeploymentCommand createDeploymentCommand) {

        SmartEngine smartEngine = processEngineConfiguration.getSmartEngine();
        RepositoryCommandService repositoryCommandService =  smartEngine.getRepositoryCommandService();

        String  processDefinitionContent = createDeploymentCommand.getProcessDefinitionContent();

        ProcessDefinition processDefinition =  repositoryCommandService
                .deployWithUTF8Content(processDefinitionContent, createDeploymentCommand.getTenantId()).getFirstProcessDefinition();

        DeploymentInstance deploymentInstance  = new DefaultDeploymentInstance();

        processEngineConfiguration.getIdGenerator().generate(deploymentInstance);

        if(null != createDeploymentCommand.getExtension() ){
            deploymentInstance.setExtension(createDeploymentCommand.getExtension());
            String convert = JsonToBpmnConverter.convert(createDeploymentCommand.getExtension());
            deploymentInstance.setProcessDefinitionContent(convert);

        }else {


            deploymentInstance.setProcessDefinitionContent(processDefinitionContent);

        }


        deploymentInstance.setProcessDefinitionId(processDefinition.getId());
        deploymentInstance.setProcessDefinitionVersion(processDefinition.getVersion());

        deploymentInstance.setProcessDefinitionName(createDeploymentCommand.getProcessDefinitionName());
        deploymentInstance.setProcessDefinitionDesc(createDeploymentCommand.getProcessDefinitionDesc());
        deploymentInstance.setProcessDefinitionType(createDeploymentCommand.getProcessDefinitionType());
        deploymentInstance.setProcessDefinitionCode(createDeploymentCommand.getProcessDefinitionCode());
        deploymentInstance.setTenantId(createDeploymentCommand.getTenantId());

        deploymentInstance.setDeploymentUserId(createDeploymentCommand.getDeploymentUserId());

        deploymentInstance.setDeploymentStatus(createDeploymentCommand.getDeploymentStatus());
        deploymentInstance.setLogicStatus(LogicStatusConstant.VALID);


        deploymentInstance = deploymentInstanceStorage.insert(deploymentInstance, smartEngine.getProcessEngineConfiguration());

        return deploymentInstance;
    }

    @Override
    public DeploymentInstance updateDeployment(UpdateDeploymentCommand updateDeploymentCommand) {

        String   deployInstanceId =  updateDeploymentCommand.getDeployInstanceId();
        DeploymentInstance currentDeploymentInstance = deploymentInstanceStorage.findById(deployInstanceId,
                updateDeploymentCommand.getTenantId(),processEngineConfiguration);

        buildUpdateValue(currentDeploymentInstance,updateDeploymentCommand);

        if(null == currentDeploymentInstance){
            throw  new EngineException("Can't find a deploymentInstance by deployInstanceId: "+deployInstanceId);
        }

        DeploymentInstance deploymentInstance =  deploymentInstanceStorage.update(currentDeploymentInstance,
            processEngineConfiguration);

        if(DeploymentStatusConstant.ACTIVE.equals(deploymentInstance.getDeploymentStatus())){
            String processDefinitionContent = updateDeploymentCommand.getProcessDefinitionContent();
            if(StringUtil.isNotEmpty(processDefinitionContent)){
                RepositoryCommandService repositoryCommandService =  processEngineConfiguration.getSmartEngine().getRepositoryCommandService();
                repositoryCommandService.deployWithUTF8Content(processDefinitionContent,updateDeploymentCommand.getTenantId());

            }
        }

        return deploymentInstance;
    }

    private void buildUpdateValue(DeploymentInstance currentDeploymentInstance,
                                  UpdateDeploymentCommand updateDeploymentCommand) {
        if (updateDeploymentCommand.getExtension() != null) {
            currentDeploymentInstance.setExtension(updateDeploymentCommand.getExtension());
            String convert = JsonToBpmnConverter.convert(updateDeploymentCommand.getExtension());
            currentDeploymentInstance.setProcessDefinitionContent(convert);

        }else{
            if (updateDeploymentCommand.getProcessDefinitionContent() != null) {
                currentDeploymentInstance.setProcessDefinitionContent(updateDeploymentCommand.getProcessDefinitionContent());
            }
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
        inactivateDeploymentInstance(deploymentInstanceId,null);
    }
    @Override
    public void inactivateDeploymentInstance(String deploymentInstanceId,String tenantId) {
        DeploymentInstance currentDeploymentInstance = deploymentInstanceStorage.findById(deploymentInstanceId,tenantId,
            processEngineConfiguration);

        if(null == currentDeploymentInstance){
            throw  new EngineException("Can't find a deploymentInstance by deployInstanceId: "+deploymentInstanceId);
        }

        currentDeploymentInstance.setDeploymentStatus(DeploymentStatusConstant.INACTIVE);
        deploymentInstanceStorage.update(currentDeploymentInstance, processEngineConfiguration);

        //processDefinitionContainer.uninstall(currentDeploymentInstance.getProcessDefinitionId(),currentDeploymentInstance.getProcessDefinitionVersion());
    }


    @Override
    public void activateDeploymentInstance(String deploymentInstanceId){
        activateDeploymentInstance(deploymentInstanceId,null);
    }
    @Override
    public void activateDeploymentInstance(String deploymentInstanceId,String tenantId) {

        RepositoryCommandService repositoryCommandService =  processEngineConfiguration.getSmartEngine().getRepositoryCommandService();

        DeploymentInstance currentDeploymentInstance = deploymentInstanceStorage.findById(deploymentInstanceId,tenantId,
            processEngineConfiguration);

        if(null == currentDeploymentInstance){
            throw  new EngineException("Can't find a deploymentInstance by deployInstanceId: "+deploymentInstanceId);
        }

        currentDeploymentInstance.setDeploymentStatus(DeploymentStatusConstant.ACTIVE);
        deploymentInstanceStorage.update(currentDeploymentInstance, processEngineConfiguration);

        repositoryCommandService.deployWithUTF8Content(currentDeploymentInstance.getProcessDefinitionContent(),currentDeploymentInstance.getTenantId());

    }

    @Override
    public void deleteDeploymentInstanceLogically(String deploymentInstanceId) {
        this.deleteDeploymentInstanceLogically(deploymentInstanceId,null);
    }

    @Override
    public void deleteDeploymentInstanceLogically(String deploymentInstanceId,String tenantId) {

        DeploymentInstance currentDeploymentInstance = deploymentInstanceStorage.findById(deploymentInstanceId,tenantId,
            processEngineConfiguration);

        if(null == currentDeploymentInstance){
            throw  new EngineException("Can't find a deploymentInstance by deployInstanceId: "+deploymentInstanceId);
        }

        currentDeploymentInstance.setLogicStatus(LogicStatusConstant.DELETED);
        currentDeploymentInstance.setDeploymentStatus(DeploymentStatusConstant.DELETED);
        deploymentInstanceStorage.update(currentDeploymentInstance, processEngineConfiguration);

        if(StringUtil.isEmpty(tenantId)){
            processDefinitionContainer.uninstall(currentDeploymentInstance.getProcessDefinitionId(),
                    currentDeploymentInstance.getProcessDefinitionVersion());
        }else{
            processDefinitionContainer.uninstall(currentDeploymentInstance.getProcessDefinitionId(),
                    currentDeploymentInstance.getProcessDefinitionVersion(),currentDeploymentInstance.getTenantId());
        }
    }


    private ProcessDefinitionContainer processDefinitionContainer;


    @Override
    public void start() {
        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
        this.processDefinitionContainer = annotationScanner.getExtensionPoint(ExtensionConstant.SERVICE,ProcessDefinitionContainer.class);
        this.deploymentInstanceStorage=annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,DeploymentInstanceStorage.class);

    }

    @Override
    public void stop() {

    }

    private ProcessEngineConfiguration processEngineConfiguration;
    private  DeploymentInstanceStorage deploymentInstanceStorage;

    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
}
