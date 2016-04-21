package com.alibaba.smart.framework.process.model.bpmn.assembly.gateway;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.process.model.bpmn.NameSpaceConstant;
import com.alibaba.smart.framework.process.model.bpmn.assembly.activity.ProcessActivity;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 * TODO gateway 可需要一个单独的父类
 */
public class ParallelGateway extends ProcessActivity {

     
    private static final long serialVersionUID = 4234776128556310813L;
    
    public final static QName type = new QName(NameSpaceConstant.NAME_SPACE,"parallelGateway");


}
