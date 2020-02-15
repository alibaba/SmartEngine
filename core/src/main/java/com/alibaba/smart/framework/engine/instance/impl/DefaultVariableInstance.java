package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.model.instance.VariableInstance;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultVariableInstance extends AbstractLifeCycleInstance implements VariableInstance {


    private String processInstanceId;

    private String executionInstanceId;

    private String fieldKey;

    private Class fieldType;

    private Object fieldValue;

}
