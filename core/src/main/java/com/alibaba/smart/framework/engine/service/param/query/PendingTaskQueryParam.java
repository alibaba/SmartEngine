package com.alibaba.smart.framework.engine.service.param.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/** Created by 高海军 帝奇 74394 on 2017 October 11:23. */
@Data
@EqualsAndHashCode(callSuper = true)
public class PendingTaskQueryParam extends BaseQueryParam {

    /** 任务处理者的用户Id */
    private String assigneeUserId;

    private List<String> assigneeGroupIdList;

    private String processDefinitionType;

    private List<String> processInstanceIdList;
}
