package com.alibaba.smart.framework.engine.ecology.designer.bpmn;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * BPMN排他网关
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExclusiveGateway extends AbstractBpmnElement {
    
    @Override
    public String getElementType() {
        return "exclusiveGateway";
    }
}
