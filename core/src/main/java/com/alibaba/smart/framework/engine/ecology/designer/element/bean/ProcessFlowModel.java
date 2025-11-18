package com.alibaba.smart.framework.engine.ecology.designer.element.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

/**
 * JSON流程模型 - 对应前端JSON格式
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class ProcessFlowModel {
    private String name;
    private String key;
    private Integer version;
    private String status;
    private List<FlowNode> nodes;
    private List<FlowEdge> edges;
}
