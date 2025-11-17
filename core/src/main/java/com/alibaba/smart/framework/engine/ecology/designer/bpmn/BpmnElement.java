package com.alibaba.smart.framework.engine.ecology.designer.bpmn;

import java.util.List;
import java.util.Map;

/**
 * BPMN元素接口
 */
public interface BpmnElement {
    
    /**
     * 获取元素ID
     */
    String getId();
    
    /**
     * 获取元素类型（XML标签名）
     */
    String getElementType();
    
    /**
     * 获取元素属性
     */
    Map<String, String> getAttributes();
    
    /**
     * 获取子元素
     */
    List<BpmnElement> getChildren();
    
    /**
     * 获取扩展元素
     */
    ExtensionElements getExtensionElements();
}
