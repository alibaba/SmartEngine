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
public class DeploymentInstanceQueryParam extends BaseQueryParam {

    private String processDefinitionVersion;

    private String processDefinitionType;

    private String processDefinitionCode;

    private String processDefinitionName;

    /** processDefinitionName like "%${processDefinitionNameLike}%" */
    private String processDefinitionNameLike;

    /** processDefinitionDesc like "%${processDefinitionDescLike}%" */
    private String processDefinitionDescLike;

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
