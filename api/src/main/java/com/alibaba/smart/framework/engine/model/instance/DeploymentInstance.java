package com.alibaba.smart.framework.engine.model.instance;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  10:03.
 */
public interface  DeploymentInstance  extends  LifeCycleInstance{

    String getProcessDefinitionId();

    void setProcessDefinitionId(String processDefinitionId);

    void setProcessDefinitionVersion(String processDefinitionVersion);

    String getProcessDefinitionVersion();


    String getProcessDefinitionType();

    void setProcessDefinitionType(String processDefinitionType);

    String getProcessDefinitionName();

    void setProcessDefinitionName(String processDefinitionName);


    String getProcessDefinitionDesc();

    void setProcessDefinitionDesc(String processDefinitionDesc);

    String getDeploymentUserId();

    void setDeploymentUserId(String deploymentUserId);

    String getProcessDefinitionContent();

    void setProcessDefinitionContent(String processDefinitionContent);

    String getDeploymentStatus();

    void setDeploymentStatus(String  deploymentStatus);

    String getLogicStatus();

    void setLogicStatus(String logicStatus);


}
