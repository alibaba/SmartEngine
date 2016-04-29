package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process;

import com.alibaba.smart.framework.engine.assembly.Handler;
import com.alibaba.smart.framework.engine.assembly.impl.AbstractActivity;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.extension.ExtensionElements;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractBpmnActivity extends AbstractActivity {
    private Handler           handler;
    private ExtensionElements extensions;
}
