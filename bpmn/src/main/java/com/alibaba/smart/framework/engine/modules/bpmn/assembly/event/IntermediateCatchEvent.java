package com.alibaba.smart.framework.engine.modules.bpmn.assembly.event;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ${DESCRIPTION}
 *
 * @author zilong.jiangzl
 * @create 2019-05-21 上午10:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IntermediateCatchEvent extends AbstractEvent {

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "intermediateCatchEvent");

    private static final long serialVersionUID = 3529827842581828899L;

    private String className;
}
