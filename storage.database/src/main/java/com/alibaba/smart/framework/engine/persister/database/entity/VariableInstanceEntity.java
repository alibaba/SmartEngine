package com.alibaba.smart.framework.engine.persister.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VariableInstanceEntity extends BaseProcessEntity {

    private Long processInstanceId;

    private Long executionInstanceId;

    private String fieldKey;

    private String fieldType;

    private Long fieldLongValue;

    private Double fieldDoubleValue;

    private String fieldStringValue;

}
