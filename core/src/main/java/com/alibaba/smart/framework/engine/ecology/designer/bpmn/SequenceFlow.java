package com.alibaba.smart.framework.engine.ecology.designer.bpmn;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Map;

/**
 * BPMN顺序流
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SequenceFlow extends AbstractBpmnElement {
    private String sourceRef;
    private String targetRef;
    private ConditionExpression conditionExpression;
    
    @Override
    public String getElementType() {
        return "sequenceFlow";
    }
    
    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attrs = super.getAttributes();
        if (sourceRef != null) {
            attrs.put("sourceRef", sourceRef);
        }
        if (targetRef != null) {
            attrs.put("targetRef", targetRef);
        }
        return attrs;
    }
}
