package com.alibaba.smart.framework.engine.ecology.designer.element.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 节点位置信息
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class Position {
    private Double x;
    private Double y;
}
