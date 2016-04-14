package com.alibaba.smart.framework.process.model.bpmn.task;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceTask extends Task {

    /**
     * 目前该属性用处不大,但是不排除未来作为扩展性用途
     */
    protected String implementationType;
    protected String implementation;
}
