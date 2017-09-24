package com.alibaba.smart.framework.engine.service.param;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  21:13.
 */

import lombok.Data;

@Data
public class CreateDeploymentRequest {

    //TODO 细化下包名,命名 整个包.

    private  String processDefinitionType;
    private  String processDefinitionName;
    private  String processDefinitionDesc;
    private  String processDefinitionContent;

    private  String deploymentUserId;
    private  String deploymentStatus;
}
