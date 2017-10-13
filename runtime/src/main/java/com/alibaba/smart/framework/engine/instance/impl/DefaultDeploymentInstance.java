package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;

import lombok.Data;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  21:21.
 */

@Data
public class DefaultDeploymentInstance extends  AbstractLifeCycleInstance implements DeploymentInstance {

    private  String processDefinitionId;
    private  String processDefinitionVersion;
    private  String processDefinitionType;
    private  String processDefinitionCode;
    private  String processDefinitionName;
    private  String processDefinitionDesc;
    private  String processDefinitionContent;

    private  String deploymentUserId;
    private  String deploymentStatus;
    //private  String logicStatus;

    //引擎上不需要实现租户功能. 由业务上支持,另外,可以通过流程类型来区分.

}
