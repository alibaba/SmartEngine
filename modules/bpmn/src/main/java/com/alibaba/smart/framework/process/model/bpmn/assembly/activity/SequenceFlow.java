package com.alibaba.smart.framework.process.model.bpmn.assembly.activity;

import com.alibaba.smart.framework.engine.assembly.impl.AbstractTransition;
import com.alibaba.smart.framework.process.model.bpmn.NameSpaceConstant;

import javax.xml.namespace.QName;


/**
 * @author 高海军 帝奇 Apr 21, 2016 3:08:26 PM
 */
public class SequenceFlow extends AbstractTransition {

    /**
     * 
     */
    private static final long serialVersionUID = 664248469321447390L;
    public final static QName type = new QName(NameSpaceConstant.NAME_SPACE, "sequenceFlow");
}
