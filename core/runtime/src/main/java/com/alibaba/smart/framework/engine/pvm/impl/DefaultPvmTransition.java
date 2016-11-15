package com.alibaba.smart.framework.engine.pvm.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.Transition;
import com.alibaba.smart.framework.engine.provider.TransitionBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DefaultPvmTransition extends AbstractPvmInvocable<Transition> implements PvmTransition {

    private PvmActivity source;
    private PvmActivity target;
    private TransitionBehavior transitionBehavior;

    @Override
    public void execute(ExecutionContext context) {
        this.fireEvent(PvmEventConstant.TRANSITION_START.name(), context);
        this.fireEvent(PvmEventConstant.TRANSITION_EXECUTE.name(), context);
        this.fireEvent(PvmEventConstant.TRANSITION_END.name(), context);
    }

    //TODO
    @Override
    public void fireEvent(String event, ExecutionContext context) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
