package com.alibaba.smart.framework.engine.ecology.designer.bpmn;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * BPMN接收任务
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReceiveTask extends AbstractBpmnElement {
    
    @Override
    public String getElementType() {
        return "receiveTask";
    }
}
