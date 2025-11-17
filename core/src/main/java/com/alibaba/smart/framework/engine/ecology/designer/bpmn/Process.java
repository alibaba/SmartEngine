package com.alibaba.smart.framework.engine.ecology.designer.bpmn;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * BPMN Process元素
 */
@Data
public class Process {
    private String id;
    private String version;
    private String name;
    private List<BpmnElement> elements = new ArrayList<>();
}
