package com.alibaba.smart.framework.engine.service.param.query;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TaskItemInstanceQueryParam extends PaginateQueryParam {

   private List<String> processInstanceIdList;

   private String activityInstanceId;

   private String processDefinitionType;

   private String processDefinitionActivityId;

   private String taskInstanceId;
   /**
    * @see com.alibaba.smart.framework.engine.constant.TaskInstanceConstant
    */
   private String status;

   private String claimUserId;

   private String tag;

   private String subBizId;

   private String bizId;
}