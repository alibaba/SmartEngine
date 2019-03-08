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
    * 子单据id列表
    */
   private List<String> subBizIdList;

   /**
    * 主单据id列表
    */
   private List<String> bizIdList;

   /**
    * @see com.alibaba.smart.framework.engine.constant.TaskInstanceConstant
    */
   private String status;

   private String claimUserId;

   private String tag;

   private String subBizId;

   private String bizId;
}