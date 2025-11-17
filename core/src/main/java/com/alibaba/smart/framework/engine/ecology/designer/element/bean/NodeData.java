package com.alibaba.smart.framework.engine.ecology.designer.element.bean;

import lombok.Data;

/**
 * 节点数据
 */
@Data
public class NodeData {
    private String type;
    private String label;
    private NodeConfig config;
}
