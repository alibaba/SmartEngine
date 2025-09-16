package com.alibaba.smart.framework.engine.service.param.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class TaskInstanceQueryParam extends BaseQueryParam {

    private List<String> processInstanceIdList;

    private String activityInstanceId;

    private String processDefinitionType;

    private String processDefinitionActivityId;

    /**
     * @see com.alibaba.smart.framework.engine.constant.TaskInstanceConstant
     */
    private String status;

    private String claimUserId;

    private String tag;

    private String extension;

    private Integer priority;

    private String comment;

    private String title;
}
