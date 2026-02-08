package com.alibaba.smart.framework.engine.service.param.query;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.alibaba.smart.framework.engine.service.param.query.JsonCondition;
import com.alibaba.smart.framework.engine.service.param.query.JsonInCondition;

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

    // ============ domain_code filters ============

    private String domainCode;

    private List<String> domainCodeList;

    private String domainCodeLike;

    // ============ extra JSON conditions (built by Dialect) ============

    private List<JsonCondition> jsonConditions;

    private List<JsonInCondition> jsonInConditions;

    private List<JsonCondition> jsonLikeConditions;

}
