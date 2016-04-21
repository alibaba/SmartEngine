package com.alibaba.smart.framework.process.model.bpmn.assembly.gateway;

import javax.xml.namespace.QName;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.assembly.impl.AbstractBase;
import com.alibaba.smart.framework.process.model.bpmn.NameSpaceConstant;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConditionExpression  extends AbstractBase{

    
    private static final long serialVersionUID = -6152070683207905381L;
    
    public final static QName type = new QName(NameSpaceConstant.NAME_SPACE,"conditionExpression");

    

    private String expressionType;

    private String expressionContent;
}
