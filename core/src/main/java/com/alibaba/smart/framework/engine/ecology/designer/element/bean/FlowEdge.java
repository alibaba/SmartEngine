package com.alibaba.smart.framework.engine.ecology.designer.element.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.Map;

/**
 * 流程连线
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
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
