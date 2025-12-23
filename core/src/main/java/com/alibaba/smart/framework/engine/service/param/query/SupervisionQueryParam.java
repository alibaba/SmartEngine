package com.alibaba.smart.framework.engine.service.param.query;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 督办查询参数
 * 
 * @author SmartEngine Team
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SupervisionQueryParam extends BaseQueryParam {

    /**
     * 督办人用户ID
     */
    private String supervisorUserId;

    /**
     * 任务实例ID列表
     */
    private List<String> taskInstanceIdList;

    /**
     * 流程实例ID列表
     */
    private List<String> processInstanceIdList;

    /**
     * 督办类型
     */
    private String supervisionType;

    /**
     * 督办状态
     */
    private String status;

    /**
     * 督办开始时间
     */
    private Date supervisionStartTime;

    /**
     * 督办结束时间
     */
    private Date supervisionEndTime;
}