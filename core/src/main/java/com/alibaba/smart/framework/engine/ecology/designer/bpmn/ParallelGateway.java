package com.alibaba.smart.framework.engine.ecology.designer.bpmn;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * BPMN并行网关
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ParallelGateway extends AbstractBpmnElement {
    
    @Override
    public String getElementType() {
        return "parallelGateway";
    }
}
