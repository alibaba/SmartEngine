package com.alibaba.smart.framework.engine.service.param.query;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class TaskInstanceQueryParam extends BaseQueryParam {

   private List<? extends Serializable> processInstanceIdList;

   private Serializable activityInstanceId;

   private String processDefinitionType;

   private String processDefinitionActivityId;
   /**
    * @see com.alibaba.smart.framework.engine.constant.TaskInstanceConstant
    */
   private String status;

   /**
    * Filter by multiple task statuses.
    */
   private List<String> statusList;

   private String claimUserId;

   private String tag;

   private String extension;

   private Integer priority;

   private String comment;

   private String title;

   /**
    * 完成时间开始
    */
   private Date completeTimeStart;

   /**
    * 完成时间结束
    */
   private Date completeTimeEnd;

}
