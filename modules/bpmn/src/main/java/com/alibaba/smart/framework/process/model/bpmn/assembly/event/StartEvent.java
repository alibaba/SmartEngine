package com.alibaba.smart.framework.process.model.bpmn.assembly.event;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.assembly.impl.AbstractActivity;
import com.alibaba.smart.framework.process.model.bpmn.NameSpaceConstant;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 * TODO 单独拿出一个来?  AbstractActivity 没有startActivity 这个属性? 有些sid编译告警.
 */
public class StartEvent extends AbstractActivity {
    
    //TODO 通过接口,而不是一个默认的属性来指定 type
    
    public final static QName type = new QName(NameSpaceConstant.NAME_SPACE,"startEvent");

    private static final long serialVersionUID = 8769494440379002970L;

}
