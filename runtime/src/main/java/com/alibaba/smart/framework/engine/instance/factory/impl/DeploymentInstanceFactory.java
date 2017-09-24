package com.alibaba.smart.framework.engine.instance.factory.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.param.CreateDeploymentRequest;

/**
 * 流程实例工厂 Created by ettear on 16-4-20.
 */
public interface DeploymentInstanceFactory {

    /**
     * 创建流程实例
     *
     * @return 流程实例
     */
    DeploymentInstance  create(CreateDeploymentRequest createDeploymentRequest);


}
