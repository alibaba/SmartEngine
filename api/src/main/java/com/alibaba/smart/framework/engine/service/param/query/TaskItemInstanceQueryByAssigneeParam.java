package com.alibaba.smart.framework.engine.service.param.query;

import java.util.List;

import lombok.Data;

/**
 * Created by jerry.zzy on 2017/11/16.
 */
@Data
public class TaskItemInstanceQueryByAssigneeParam extends PaginateQueryParam {

    private String assigneeUserId;

    private List<String> assigneeGroupIdList;

    private String processDefinitionType;

    private List<Long> processInstanceIdList;

    private String status;

    private Long taskInstanceId;

    private String subBizId;

    private String bizId;

}
