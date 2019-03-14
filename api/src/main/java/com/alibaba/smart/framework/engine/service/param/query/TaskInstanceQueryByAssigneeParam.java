package com.alibaba.smart.framework.engine.service.param.query;

import com.alibaba.smart.framework.engine.service.param.query.condition.CustomFieldCondition;
import lombok.Data;

import java.util.List;

/**
 * Created by jerry.zzy on 2017/11/16.
 */
@Data
public class TaskInstanceQueryByAssigneeParam extends PaginateQueryParam {

    private String assigneeUserId;

    private List<String> assigneeGroupIdList;

    private String processDefinitionType;

    private List<Long> processInstanceIdList;

    private String status;

    /**
     * 扩展字段查询
     */
    private List<CustomFieldCondition> customFieldConditionList;

}
