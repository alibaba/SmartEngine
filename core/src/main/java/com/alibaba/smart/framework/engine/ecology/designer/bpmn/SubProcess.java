package com.alibaba.smart.framework.engine.ecology.designer.bpmn;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * BPMN子流程
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SubProcess extends AbstractBpmnElement {
    
    @Override
    public String getElementType() {
        return "subProcess";
    }
}
