package com.alibaba.smart.framework.engine.service.param.query;

import java.util.List;

import com.alibaba.smart.framework.engine.service.param.PaginateRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class TaskInstanceQueryParam extends PaginateRequest {

   private Long processInstanceId;

   private Long activityInstanceId;


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
