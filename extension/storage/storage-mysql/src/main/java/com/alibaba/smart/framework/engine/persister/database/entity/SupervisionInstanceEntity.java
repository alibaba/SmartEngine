package com.alibaba.smart.framework.engine.persister.database.entity;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 督办记录实体类
 * 
 * @author SmartEngine Team
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SupervisionInstanceEntity extends BaseProcessEntity {

    private Long processInstanceId;

    private Long taskInstanceId;

    private String supervisorUserId;

    private String supervisionReason;

    private String supervisionType;

    private String status;

    private Date closeTime;
}