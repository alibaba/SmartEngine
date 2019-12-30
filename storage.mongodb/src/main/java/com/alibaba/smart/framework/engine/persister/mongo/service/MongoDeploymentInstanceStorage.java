package com.alibaba.smart.framework.engine.persister.mongo.service;


import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.DeploymentInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.service.param.query.DeploymentInstanceQueryParam;

/**
 * Created by 高海军 帝奇 74394 on 2018 October  20:32.
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = DeploymentInstanceStorage.class)

public class MongoDeploymentInstanceStorage implements DeploymentInstanceStorage {

    private static final String INSTANCE = "se_deployment_instance";

    @Override
    public DeploymentInstance insert(DeploymentInstance deploymentInstance,
                                     ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public DeploymentInstance update(DeploymentInstance deploymentInstance,
                                     ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public DeploymentInstance findById(String id, ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public List<DeploymentInstance> findByPage(DeploymentInstanceQueryParam deploymentInstanceQueryParam,
                                               ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public int count(DeploymentInstanceQueryParam deploymentInstanceQueryParam,
                     ProcessEngineConfiguration processEngineConfiguration) {
        return 0;
    }

    @Override
    public void remove(String id, ProcessEngineConfiguration processEngineConfiguration) {

    }
}