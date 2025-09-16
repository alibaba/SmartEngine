package com.alibaba.smart.framework.engine.bpmn.assembly.multi.instance;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.NoneIdBasedElement;

import javax.xml.namespace.QName;

/**
 * @author ettear Created by ettear on 16/10/2017.
 */
public class InputDataItem implements NoneIdBasedElement {
    public static final QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE, "inputDataItem");

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
