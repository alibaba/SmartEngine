package com.alibaba.smart.framework.engine.pvm.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.Transition;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by ettear on 16-4-13.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DefaultPvmTransition extends AbstractPvmInvocable<Transition> implements PvmTransition {

    private PvmActivity source;
    private PvmActivity target;

    @Override
    public void execute(ExecutionContext context) {
        this.fireEvent(PvmEventConstant.TRANSITION_START.name(), context);
        this.fireEvent(PvmEventConstant.TRANSITION_EXECUTE.name(), context);
        this.fireEvent(PvmEventConstant.TRANSITION_END.name(), context);
    }
}
