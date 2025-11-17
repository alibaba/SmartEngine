package com.alibaba.smart.framework.engine.ecology.designer.bpmn;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Map;

/**
 * BPMN结束事件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EndEvent extends AbstractBpmnElement {
    private Boolean terminateAll;
    
    @Override
    public String getElementType() {
        return "endEvent";
    }
    
    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attrs = super.getAttributes();
        if (terminateAll != null && terminateAll) {
            attrs.put("terminateAll", "true");
        }
        return attrs;
    }
}
