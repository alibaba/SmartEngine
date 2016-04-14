package com.alibaba.smart.framework.flow.model.bpmn.task;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceTask extends Task {

    /**
     * 目前该属性用处不大,但是不排除未来作为扩展性用途
     */
    protected String implementationType;
    protected String implementation;
}
