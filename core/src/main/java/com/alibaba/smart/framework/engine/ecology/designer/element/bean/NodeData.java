package com.alibaba.smart.framework.engine.ecology.designer.element.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 节点数据
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class NodeData {
    private String type;
    private String label;
    private NodeConfig config;
}
