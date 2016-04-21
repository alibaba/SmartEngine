package com.alibaba.smart.framework.process.model.bpmn.assembly.event;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.assembly.impl.AbstractActivity;
import com.alibaba.smart.framework.process.model.bpmn.NameSpaceConstant;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:51:07 PM
 */
public class EndEvent extends AbstractActivity {
    public final static QName type = new QName(NameSpaceConstant.NAME_SPACE,"endEvent");

    
    private static final long serialVersionUID = 3529827842581828898L;

}
