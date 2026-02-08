package com.alibaba.smart.framework.engine.service.query;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.service.param.query.DeploymentInstanceQueryParam;

/**
 * 查询流程定义的部署实例。
 *
 * Created by 高海军 帝奇 74394 on 2017 September  17:15.
 */
public interface DeploymentQueryService {

    /**
     * @deprecated Use {@code smartEngine.createDeploymentQuery().deploymentInstanceId(id).singleResult()} instead
     */
    @Deprecated
    DeploymentInstance findById(String deploymentInstanceId);

    /**
     * @deprecated Use {@code smartEngine.createDeploymentQuery().deploymentInstanceId(id).tenantId(t).singleResult()} instead
     */
    @Deprecated
    DeploymentInstance findById(String deploymentInstanceId,String tenantId);

    /**
     * @deprecated Use {@code smartEngine.createDeploymentQuery()...list()} instead
     */
    @Deprecated
    List<DeploymentInstance> findList(DeploymentInstanceQueryParam deploymentInstanceQueryParam) ;

    /**
     * @deprecated Use {@code smartEngine.createDeploymentQuery()...count()} instead
     */
    @Deprecated
    Integer count(DeploymentInstanceQueryParam deploymentInstanceQueryParam);
}
