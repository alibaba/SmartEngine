package com.alibaba.smart.framework.engine.instance.impl;

import java.util.Date;

import com.alibaba.smart.framework.engine.model.instance.TaskTransferRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 默认任务移交记录实现
 *
 * @author SmartEngine Team
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DefaultTaskTransferRecord extends AbstractLifeCycleInstance implements TaskTransferRecord {

    private static final long serialVersionUID = 1L;

    private String processInstanceId;

    private String taskInstanceId;

    private String fromUserId;

    private String toUserId;

    private String transferReason;

    private Date deadline;
}
