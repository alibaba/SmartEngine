package com.alibaba.smart.framework.engine.service.param;

import lombok.Data;
import lombok.ToString;

/**
 * Created by yueyu.yr on 2017/9/22.
 *
 * @author yueyu.yr
 * @date 2017/09/22
 */
@Data
@ToString(callSuper = true)
public class DeploymentInstanceParam extends PaginateRequest {

    private Long id;

    private String processDefinitionVersion;

    private String processDefinitionType;

    private String processDefinitionName;

    private String deploymentUserId;

    /**
     * @see com.alibaba.smart.framework.engine.constant.DeploymentStatusConstant
     */
    private String deploymentStatus;

    /**
     * @see com.alibaba.smart.framework.engine.constant.LogicStatusConstant
     */
    private String logicStatus;
}
