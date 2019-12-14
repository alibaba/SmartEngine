package com.alibaba.smart.framework.engine.service.param.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by yueyu.yr on 2017/9/22.
 *
 * @author yueyu.yr
 * @date 2017/09/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeploymentInstanceQueryParam extends PaginateQueryParam {

    private Long id;

    private String processDefinitionVersion;

    private String processDefinitionType;

    private String processDefinitionCode;

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
