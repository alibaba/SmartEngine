package com.alibaba.smart.framework.engine.ecology.designer.element.bean;

import lombok.Data;
import java.util.Map;

/**
 * 流程连线
 */
@Data
public class FlowEdge {
    private String id;
    private String source;
    private String target;
    private String type;
    private Boolean animated;
    private String label;
    private Map<String, Object> style;
    private EdgeData data;
}
