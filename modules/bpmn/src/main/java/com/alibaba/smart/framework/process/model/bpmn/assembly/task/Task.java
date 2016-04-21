package com.alibaba.smart.framework.process.model.bpmn.assembly.task;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.process.model.bpmn.assembly.activity.ProcessActivity;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Task extends ProcessActivity {

    /**
     * 
     */
    private static final long serialVersionUID = 5042056118774610434L;
    private boolean isAsync;
}
