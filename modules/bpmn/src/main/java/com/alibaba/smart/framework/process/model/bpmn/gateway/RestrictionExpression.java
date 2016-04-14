package com.alibaba.smart.framework.process.model.bpmn.gateway;

import lombok.Data;

@Data
public class RestrictionExpression {

    private String expressionType;

    private String expressionContent;
}
