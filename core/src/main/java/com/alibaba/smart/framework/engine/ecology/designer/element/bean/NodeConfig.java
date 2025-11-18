package com.alibaba.smart.framework.engine.ecology.designer.element.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.Map;

/**
 * 节点配置
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)

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
