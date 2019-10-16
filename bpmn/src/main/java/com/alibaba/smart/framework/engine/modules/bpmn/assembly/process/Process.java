package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process;

import java.util.List;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;

/**
 * @author 高海军 帝奇 Apr 21, 2016 1:37:18 PM TODO 这个需要考虑下,process 自身不是一个activity,但是subprocess 被当成一个activity
 */
@Data
public class Process implements com.alibaba.smart.framework.engine.model.assembly.Process {

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "process");

    private static final long serialVersionUID = -2660788294142169268L;

    private String id;
    private String name;

    private List<BaseElement> elements;
}
