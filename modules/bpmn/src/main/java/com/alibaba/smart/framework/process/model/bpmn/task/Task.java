package com.alibaba.smart.framework.process.model.bpmn.task;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.process.model.bpmn.ProcessActivity;

@EqualsAndHashCode(callSuper = true)
@Data
public class Task extends ProcessActivity {

    private boolean isAsync;
}
