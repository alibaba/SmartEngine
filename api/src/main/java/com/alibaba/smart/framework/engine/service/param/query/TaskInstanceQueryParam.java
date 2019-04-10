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

   private String taskInstanceId;

   /**
    * 自定义字段查询，包括查询条件和系统扩展字段
    */
   private CustomFieldsQueryParam customFieldsQueryParam;

}