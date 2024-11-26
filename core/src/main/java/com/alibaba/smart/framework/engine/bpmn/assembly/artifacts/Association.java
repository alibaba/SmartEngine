package com.alibaba.smart.framework.engine.bpmn.assembly.artifacts;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnConstants;  // Import the new constants class
import com.alibaba.smart.framework.engine.model.assembly.NoneIdBasedElement;
import lombok.Data;

import javax.xml.namespace.QName;

@Data
public class Association implements NoneIdBasedElement {

    private static final long serialVersionUID = 6357539757300185621L;

    // Use the constant from BpmnConstants class
    public final static QName qtype = BpmnConstants.ASSOCIATION_QTYPE;
}
