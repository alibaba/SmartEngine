package com.alibaba.smart.framework.engine.service.query;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.service.param.DeploymentInstanceParam;
import com.alibaba.smart.framework.engine.service.param.PaginateRequest;

/**
 * 查询流程定义的部署实例。
 *
 * Created by 高海军 帝奇 74394 on 2017 September  17:15.
 */
public interface DeploymentQueryService {

    DeploymentInstance findOne(Long deploymentInstanceId);

    List<DeploymentInstance> findDeploymentList(DeploymentInstanceParam deploymentInstanceParam) ;

    Integer queryDeploymentInstanceCount(DeploymentInstanceParam deploymentInstanceParam);
}
