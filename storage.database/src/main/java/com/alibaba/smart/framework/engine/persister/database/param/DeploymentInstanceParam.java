package com.alibaba.smart.framework.engine.persister.database.param;

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
public class DeploymentInstanceParam extends BaseParam{

    private Long id;

    private String processDefinitionVersion;

    private String processDefinitionType;

    private String processDefinitionName;

    private String deploymentUserId;

    private String deploymentStatus;

    private String logicStatus;
}
