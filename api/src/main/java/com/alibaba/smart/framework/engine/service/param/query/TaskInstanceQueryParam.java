package com.alibaba.smart.framework.engine.service.param.query;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class TaskInstanceQueryParam extends PaginateQueryParam {

   private Long processInstanceId;

   private Long activityInstanceId;

   private String processDefinitionType;

   private String processDefinitionActivityId;
   /**
    * @see com.alibaba.smart.framework.engine.constant.TaskInstanceConstant
    */
   private String status;
   /**
    * 任务处理者的用户Id
    */
   private String assigneeUserId;

   private List<String> assigneeGroupIdList;

   private String tag;
}