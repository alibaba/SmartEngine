package com.alibaba.smart.framework.engine.service.param.query;

import java.util.List;

import com.alibaba.smart.framework.engine.service.param.query.condition.CustomFieldCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class TaskInstanceQueryParam extends PaginateQueryParam {

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

   /**
    * 扩展字段查询
    */
   private List<CustomFieldCondition> customFieldConditionList;

}