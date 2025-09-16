package com.alibaba.smart.framework.engine.bpmn.assembly.multi.instance;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.NoneIdBasedElement;

import javax.xml.namespace.QName;

/**
 * @author ettear Created by ettear on 16/10/2017.
 */
public class LoopDataInputRef implements NoneIdBasedElement {
    public static final QName qtype =
            new QName(BpmnNameSpaceConstant.NAME_SPACE, "loopDataInputRef");
    private String reference;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
