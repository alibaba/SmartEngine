package com.alibaba.smart.framework.engine.service.param.query;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 已办任务查询参数
 * 
 * @author SmartEngine Team
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CompletedTaskQueryParam extends BaseQueryParam {

    /**
     * 任务处理人用户ID
     */
    private String claimUserId;

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
     * 任务标题（模糊查询）
     */
    private String title;

    /**
     * 任务标签
     */
    private String tag;

    /**
     * 处理意见（模糊查询）
     */
    private String comment;
}