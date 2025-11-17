package com.alibaba.smart.framework.engine.ecology.designer.element.bean;

import lombok.Data;

/**
 * 任务分配人配置
 */
@Data
public class AssigneeConfig {
    private String type;
    private Boolean multi;
    private String assigneeMode;
    private String value;
}
