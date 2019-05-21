package com.alibaba.smart.framework.engine.modules.bpmn.assembly.event;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

/**
 * ${DESCRIPTION}
 *
 * @author zilong.jiangzl
 * @create 2019-05-21 下午9:19
 */
public class IntermediateThrowEvent extends AbstractEvent {

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "intermediateThrowEvent");

    private static final long serialVersionUID = 3529827842581828899L;

    private String className;
}