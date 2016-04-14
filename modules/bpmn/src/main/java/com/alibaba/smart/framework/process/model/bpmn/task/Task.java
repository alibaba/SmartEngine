package com.alibaba.smart.framework.process.model.bpmn.task;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.process.model.bpmn.ProcessActivity;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Task extends ProcessActivity {

    private boolean isAsync;
}
