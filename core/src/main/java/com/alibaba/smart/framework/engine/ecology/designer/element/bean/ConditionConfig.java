package com.alibaba.smart.framework.engine.ecology.designer.element.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 条件配置
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConditionConfig {
    private String type;
    private String content;
}
