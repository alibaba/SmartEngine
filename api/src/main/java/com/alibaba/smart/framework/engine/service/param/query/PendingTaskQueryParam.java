package com.alibaba.smart.framework.engine.service.param.query;

import java.util.List;

import lombok.Data;

/**
 * Created by 高海军 帝奇 74394 on 2017 October  11:23.
 */
@Data
public class PendingTaskQueryParam {

    /**
     * 任务处理者的用户Id
     */
    private String assigneeUserId;

    private List<String> assigneeGroupIdList;

    private String processDefinitionType;

    private Long processInstanceId;

}