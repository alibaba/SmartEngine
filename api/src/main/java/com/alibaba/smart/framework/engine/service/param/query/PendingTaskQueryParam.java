package com.alibaba.smart.framework.engine.service.param.query;

import java.util.List;

import lombok.Data;

/**
 * Created by 高海军 帝奇 74394 on 2017 October  11:23.
 */
@Data
public class PendingTaskQueryParam extends PaginateQueryParam{

    /**
     * 任务处理者的用户Id
     */
    private String assigneeUserId;

    private List<String> assigneeGroupIdList;

    private String processDefinitionType;

    private List<String> processInstanceIdList;

    /**
     * 自定义字段查询，包括查询条件和系统扩展字段
     */
    private CustomFieldsQueryParam customFieldsQueryParam;
}