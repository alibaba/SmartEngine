package com.alibaba.smart.framework.engine.service.param.query;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class TaskInstanceQueryParam extends PaginateQueryParam {

   private List<Long> processInstanceIdList;

   private Long activityInstanceId;

   private String processDefinitionType;

   private String processDefinitionActivityId;
   /**
    * @see com.alibaba.smart.framework.engine.constant.TaskInstanceConstant
    */
   private String status;

   private String claimUserId;

   private String tag;

}