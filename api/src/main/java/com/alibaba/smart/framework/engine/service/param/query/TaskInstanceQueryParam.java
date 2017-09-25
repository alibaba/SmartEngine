package com.alibaba.smart.framework.engine.service.param.query;

import com.alibaba.smart.framework.engine.service.param.PaginateRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class TaskInstanceQueryParam extends PaginateRequest {

   private Long processInstanceId;

   /**
    * @see com.alibaba.smart.framework.engine.constant.TaskInstanceConstant
    */
   private String status;
   private String assigneeId;
   private String tag;
}
