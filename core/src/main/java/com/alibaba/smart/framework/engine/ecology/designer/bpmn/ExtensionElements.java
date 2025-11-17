package com.alibaba.smart.framework.engine.ecology.designer.bpmn;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * BPMN扩展元素 - 用于存储position等自定义信息
 */
@Data
public class ExtensionElements {
    private Map<String, Object> properties = new HashMap<>();
    
    public void addProperty(String key, Object value) {
        properties.put(key, value);
    }
    
    public Object getProperty(String key) {
        return properties.get(key);
    }
    
    public boolean hasProperties() {
        return properties != null && !properties.isEmpty();
    }
}
