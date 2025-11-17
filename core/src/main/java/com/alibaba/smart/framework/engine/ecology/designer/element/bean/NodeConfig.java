package com.alibaba.smart.framework.engine.ecology.designer.element.bean;

import lombok.Data;
import java.util.Map;

/**
 * 节点配置
 */
@Data
public class NodeConfig {
    private String name;
    private String description;
    private AssigneeConfig assignee;
    private Integer priority;
    private Boolean skipable;
    private Boolean terminateAll;
    private String initiator;
    private String smartClass;
    private Map<String, Object> properties;
}
