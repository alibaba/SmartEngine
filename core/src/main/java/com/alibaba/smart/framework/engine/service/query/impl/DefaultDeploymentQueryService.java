package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.storage.DeploymentInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.service.param.query.DeploymentInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.query.DeploymentQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  07:48.
 */

@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = DeploymentQueryService.class)

public class DefaultDeploymentQueryService implements DeploymentQueryService ,
    ProcessEngineConfigurationAware, LifeCycleHook {


    private  DeploymentInstanceStorage deploymentInstanceStorage;

    @Override
    public DeploymentInstance findById(String deploymentInstanceId) {
        return this.findById(deploymentInstanceId,null);
    }

    @Override
    public DeploymentInstance findById(String deploymentInstanceId,String tenantId) {


        DeploymentInstance currentDeploymentInstance = deploymentInstanceStorage.findById(deploymentInstanceId,tenantId,
                processEngineConfiguration);
        return  currentDeploymentInstance;
    }


    @Override
    public List<DeploymentInstance> findList(DeploymentInstanceQueryParam deploymentInstanceQueryParam) {


        List<DeploymentInstance> deploymentInstanceList = deploymentInstanceStorage.findByPage(
            deploymentInstanceQueryParam, processEngineConfiguration);
        return  deploymentInstanceList;
    }

    @Override
    public Integer count(DeploymentInstanceQueryParam deploymentInstanceQueryParam) {



        int count = deploymentInstanceStorage.count(deploymentInstanceQueryParam, processEngineConfiguration);
        return count;
    }

    private ProcessEngineConfiguration processEngineConfiguration;

    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }

    @Override
    public void start() {
       this. deploymentInstanceStorage = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.COMMON,DeploymentInstanceStorage.class);

    }

    @Override
    public void stop() {

    }
}
