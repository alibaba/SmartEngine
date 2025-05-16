package com.alibaba.smart.framework.engine.service.param.query;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by jerry.zzy on 2017/11/16.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskInstanceQueryByAssigneeParam extends BaseQueryParam {

    private String assigneeUserId;

    private List<String> assigneeGroupIdList;

    private String processDefinitionType;

    private List<Long> processInstanceIdList;

    private String status;

}
