package com.alibaba.smart.framework.engine.ecology.designer.bpmn;

import lombok.Data;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * BPMN元素抽象基类
 */
@Data
public abstract class AbstractBpmnElement implements BpmnElement {
    protected String id;
    protected String name;
    protected ExtensionElements extensionElements;
    protected List<BpmnElement> children = new ArrayList<>();
    
    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attrs = new LinkedHashMap<>();
        if (id != null) {
            attrs.put("id", id);
        }
        if (name != null) {
            attrs.put("name", name);
        }
        return attrs;
    }
    
    @Override
    public List<BpmnElement> getChildren() {
        return children;
    }
    
    @Override
    public ExtensionElements getExtensionElements() {
        return extensionElements;
    }
}
