package com.alibaba.smart.framework.engine.ecology.designer.bpmn;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Map;

/**
 * BPMN服务任务
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceTask extends AbstractBpmnElement {
    private String smartClass;
    
    @Override
    public String getElementType() {
        return "serviceTask";
    }
    
    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attrs = super.getAttributes();
        if (smartClass != null) {
            attrs.put("smart:class", smartClass);
        }
        return attrs;
    }
}
