package com.alibaba.smart.framework.engine.runtime.impl;

import lombok.Data;

import com.alibaba.smart.framework.engine.assembly.Transition;
import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.invocation.AtomicOperationEvent;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * Created by ettear on 16-4-13.
 */
@Data
public class DefaultRuntimeTransition extends AbstractRuntimeInvocable<Transition> implements PvmTransition {

    private PvmActivity source;
    private PvmActivity target;

    @Override
    public void execute(InstanceContext context) {
        this.invoke(AtomicOperationEvent.TRANSITION_START.name(), context);
        this.invoke(AtomicOperationEvent.TRANSITION_EXECUTE.name(), context);
        this.invoke(AtomicOperationEvent.TRANSITION_END.name(), context);
    }
}
