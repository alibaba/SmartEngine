package com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractBaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

/**
 * @author ettear
 * Created by ettear on 16/10/2017.
 */
public class LoopDataInputRef extends AbstractBaseElement implements LoopCollection {
    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "loopDataInputRef");
    private String reference;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
