package com.alibaba.smart.framework.engine.persister.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by yueyu.yr on 2017/9/22.
 *
 * @author yueyu.yr
 * @date 2017/09/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeploymentInstanceEntity extends BaseProcessEntity {

    private Long id;

    private String processDefinitionVersion;

    private String processDefinitionType;

    private String processDefinitionName;

    private String processDefinitionDesc;

    private String processDefinitionContent;

    private String deploymentUserId;

    private String deploymentStatus;

    private String logicStatus;


}
