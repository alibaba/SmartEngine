package com.alibaba.smart.framework.engine.ecology.designer.bpmn;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * BPMN开始事件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StartEvent extends AbstractBpmnElement {
    
    @Override
    public String getElementType() {
        return "startEvent";
    }
}
