package com.alibaba.smart.framework.engine.ecology.designer.bpmn;

import lombok.Data;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * BPMN Definitions元素
 */
@Data
public class Definitions {
    private String id;
    private String version;
    private String targetNamespace;
    private Map<String, String> namespaces = new LinkedHashMap<>();
    private Process process;
    
    public Definitions() {
        // 默认命名空间
        namespaces.put("xmlns", "http://www.omg.org/spec/BPMN/20100524/MODEL");
        namespaces.put("xmlns:smart", "http://smartengine.org/schema/process");
        namespaces.put("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    }
}
