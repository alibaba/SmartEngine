package com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway;

import javax.xml.namespace.QName;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnBase;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 * TODO gateway 可需要一个单独的父类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ParallelGateway extends AbstractGateway {

     
    private static final long serialVersionUID = 4234776128556310813L;
    
    public final static QName type = new QName(BpmnBase.NAME_SPACE, "parallelGateway");


}
