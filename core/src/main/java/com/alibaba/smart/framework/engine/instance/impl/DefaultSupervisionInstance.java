package com.alibaba.smart.framework.engine.instance.impl;

import java.util.Date;

import com.alibaba.smart.framework.engine.model.instance.SupervisionInstance;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 默认督办实例实现
 * 
 * @author SmartEngine Team
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DefaultSupervisionInstance extends AbstractLifeCycleInstance implements SupervisionInstance {

    private static final long serialVersionUID = 1L;

    private String processInstanceId;

    private String taskInstanceId;

    private String supervisorUserId;

    private String supervisionReason;

    private String supervisionType;

    private String status;

    private Date closeTime;
}