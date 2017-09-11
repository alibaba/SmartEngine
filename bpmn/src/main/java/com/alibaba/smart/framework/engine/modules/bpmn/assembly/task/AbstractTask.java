package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task;

import com.alibaba.smart.framework.engine.model.assembly.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.AbstractBpmnActivity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractTask extends AbstractBpmnActivity {

    /**
     *
     */
    private static final long serialVersionUID = 5042056118774610434L;

    private MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics;
    public MultiInstanceLoopCharacteristics getMultiInstanceLoopCharacteristics() {
        return multiInstanceLoopCharacteristics;
    }

    public void setMultiInstanceLoopCharacteristics(
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics) {
        this.multiInstanceLoopCharacteristics = multiInstanceLoopCharacteristics;
    }


}
