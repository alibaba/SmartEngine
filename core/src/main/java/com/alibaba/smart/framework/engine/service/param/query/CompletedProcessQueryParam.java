package com.alibaba.smart.framework.engine.service.param.query;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 办结流程查询参数
 * 
 * @author SmartEngine Team
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CompletedProcessQueryParam extends BaseQueryParam {

    /**
     * 参与用户ID（发起人或处理过任务的用户）
     */
    private String participantUserId;

    /**
     * 流程定义类型列表
     */
    private List<String> processDefinitionTypes;

    /**
     * 完成时间开始
     */
    private Date completeTimeStart;

    /**
     * 完成时间结束
     */
    private Date completeTimeEnd;

    /**
     * 流程实例ID列表
     */
    private List<String> processInstanceIdList;

    /**
     * 流程标题（模糊查询）
     */
    private String title;

    /**
     * 流程标签
     */
    private String tag;

    /**
     * 业务唯一ID
     */
    private String bizUniqueId;

    /**
     * 发起人用户ID
     */
    private String startUserId;
}