package com.alibaba.smart.framework.engine.service.query;

import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.service.param.query.DeploymentInstanceQueryParam;

import java.util.List;

/**
 * 查询流程定义的部署实例。
 *
 * <p>Created by 高海军 帝奇 74394 on 2017 September 17:15.
 */
public interface DeploymentQueryService {

    DeploymentInstance findById(String deploymentInstanceId);

    DeploymentInstance findById(String deploymentInstanceId, String tenantId);

    List<DeploymentInstance> findList(DeploymentInstanceQueryParam deploymentInstanceQueryParam);

    Integer count(DeploymentInstanceQueryParam deploymentInstanceQueryParam);
}
