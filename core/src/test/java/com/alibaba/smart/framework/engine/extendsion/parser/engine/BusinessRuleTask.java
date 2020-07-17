package com.alibaba.smart.framework.engine.extendsion.parser.engine;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.bpmn.assembly.multi.instance.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractTask;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessRuleTask extends AbstractTask {

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "businessRuleTask");
    private static final long serialVersionUID = -2357571784043122485L;

    private MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics;

    @Override
    public String toString() {
        return super.getId();
    }

}