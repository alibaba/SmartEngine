package com.alibaba.smart.framework.engine.service.param.query;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.alibaba.smart.framework.engine.service.param.query.JsonCondition;
import com.alibaba.smart.framework.engine.service.param.query.JsonInCondition;


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
    * Fuzzy search on title (LIKE %titleLike%).
    */
   private String titleLike;

   /**
    * 完成时间开始
    */
   private Date completeTimeStart;

   /**
    * 完成时间结束
    */
   private Date completeTimeEnd;

   /**
    * Filter by create time range (start, inclusive).
    */
   private Date createTimeStart;

   /**
    * Filter by create time range (end, exclusive).
    */
   private Date createTimeEnd;

   /**
    * Filter by multiple process definition types (IN clause).
    */
   private List<String> processDefinitionTypeList;

   /**
    * Filter for unassigned tasks (claim_user_id IS NULL).
    */
   private Boolean unassigned;

   /**
    * Fuzzy search on claim user ID (LIKE %claimUserIdLike%).
    */
   private String claimUserIdLike;

   /**
    * Filter by minimum priority (inclusive).
    */
   private Integer minPriority;

   /**
    * Filter by maximum priority (inclusive).
    */
   private Integer maxPriority;

   // ============ domain_code filters ============

   private String domainCode;

   private List<String> domainCodeList;

   private String domainCodeLike;

   // ============ extra JSON conditions (built by Dialect) ============

   private List<JsonCondition> jsonConditions;

   private List<JsonInCondition> jsonInConditions;

   private List<JsonCondition> jsonLikeConditions;

}
