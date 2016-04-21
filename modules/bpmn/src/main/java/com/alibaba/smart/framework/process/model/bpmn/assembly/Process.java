package com.alibaba.smart.framework.process.model.bpmn.assembly;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.assembly.impl.AbstractProcess;
import com.alibaba.smart.framework.process.model.bpmn.NameSpaceConstant;


/**
 * @author 高海军 帝奇 Apr 21, 2016 1:37:18 PM
 * 
 * TODO 这个需要考虑下,process 自身不是一个activity,但是subprocess 被当成一个activity
 */
public class Process  extends AbstractProcess  {
    public final static QName type = new QName(NameSpaceConstant.NAME_SPACE,"process");

     
    private static final long serialVersionUID = -2660788294142169268L;

}
