package com.alibaba.smart.framework.engine.service.param;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  21:13.
 */

import lombok.Data;

@Data
public class CreateDeploymentRequest {

    private  String processDefinitionType;
    private  String processDefinitionName;
    private  String processDefinitionDesc;
    private  String processDefinitionContent;

    private  Long deploymentUserId;
    private  String deploymentStatus;
}
