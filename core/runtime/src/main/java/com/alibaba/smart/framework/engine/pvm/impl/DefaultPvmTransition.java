package com.alibaba.smart.framework.engine.pvm.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.model.assembly.Transition;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;

/**
 * Created by ettear on 16-4-13.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DefaultPvmTransition extends AbstractPvmInvocable<Transition> implements PvmTransition {

    private PvmActivity source;
    private PvmActivity target;

    @Override
    public void execute(InstanceContext context) {
        this.invoke(PvmEventConstant.TRANSITION_START.name(), context);
        this.invoke(PvmEventConstant.TRANSITION_EXECUTE.name(), context);
        this.invoke(PvmEventConstant.TRANSITION_END.name(), context);
    }
}