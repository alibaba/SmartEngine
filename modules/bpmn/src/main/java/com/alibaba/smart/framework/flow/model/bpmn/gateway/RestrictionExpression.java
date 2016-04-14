package com.alibaba.smart.framework.flow.model.bpmn.gateway;

import lombok.Data;

@Data
public class RestrictionExpression {

    private String expressionType;

    private String expressionContent;
}
