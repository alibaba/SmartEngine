package com.alibaba.smart.framework.engine.service.param.query;

import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.IdConverter;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class TaskInstanceQueryParam extends BaseQueryParam {

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

   // ============ Long type getters for MyBatis (avoid CAST in SQL) ============

   /**
    * Get process instance ID list as Long type.
    * Used by MyBatis to avoid CAST operation in SQL.
    *
    * @return Long type ID list, or null if source is null
    */
   public List<Long> getProcessInstanceIdListAsLong() {
      return IdConverter.toLongList(processInstanceIdList);
   }

   /**
    * Get activity instance ID as Long type.
    * Used by MyBatis to avoid CAST operation in SQL.
    *
    * @return Long type ID, or null if source is null
    */
   public Long getActivityInstanceIdAsLong() {
      return IdConverter.toLong(activityInstanceId);
   }

}