package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.model.instance.AssigneeOperationRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 默认加签减签操作记录实现
 *
 * @author SmartEngine Team
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DefaultAssigneeOperationRecord extends AbstractLifeCycleInstance implements AssigneeOperationRecord {

    private static final long serialVersionUID = 1L;

    private String processInstanceId;

    private String taskInstanceId;

    private String operationType;

    private String operatorUserId;

    private String targetUserId;

    private String operationReason;
}
