package com.alibaba.smart.framework.engine.service.param.command;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  21:13.
 */

import lombok.Data;

@Data
public class UpdateDeploymentCommand {

    private  Long   deployInstanceId;

    private  String processDefinitionType;
    private  String processDefinitionName;
    private  String processDefinitionDesc;
    private  String processDefinitionContent;

    private  String deploymentUserId;
    private  String deploymentStatus;
    //private  String logicStatus;
}
