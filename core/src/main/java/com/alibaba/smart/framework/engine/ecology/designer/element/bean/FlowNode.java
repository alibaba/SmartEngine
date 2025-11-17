package com.alibaba.smart.framework.engine.ecology.designer.element.bean;

import lombok.Data;

/**
 * 流程节点
 */
@Data
public class FlowNode {
    private String id;
    private String type;
    private Position position;
    private NodeData data;
}
