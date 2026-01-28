package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.model.instance.RollbackRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 默认流程回退记录实现
 *
 * @author SmartEngine Team
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DefaultRollbackRecord extends AbstractLifeCycleInstance implements RollbackRecord {

    private static final long serialVersionUID = 1L;

    private String processInstanceId;

    private String taskInstanceId;

    private String rollbackType;

    private String fromActivityId;

    private String toActivityId;

    private String operatorUserId;

    private String rollbackReason;
}
