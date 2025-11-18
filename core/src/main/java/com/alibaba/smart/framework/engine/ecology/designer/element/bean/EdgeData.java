package com.alibaba.smart.framework.engine.ecology.designer.element.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 连线数据
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EdgeData {
    private String label;
    private ConditionConfig condition;
}
