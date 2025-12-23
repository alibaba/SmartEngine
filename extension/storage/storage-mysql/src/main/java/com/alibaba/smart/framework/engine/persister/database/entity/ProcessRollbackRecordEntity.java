package com.alibaba.smart.framework.engine.persister.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 流程回退记录实体类
 * 
 * @author SmartEngine Team
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProcessRollbackRecordEntity extends BaseProcessEntity {

    private Long processInstanceId;

    private Long taskInstanceId;

    private String rollbackType;

    private String fromActivityId;

    private String toActivityId;

    private String operatorUserId;

    private String rollbackReason;
}