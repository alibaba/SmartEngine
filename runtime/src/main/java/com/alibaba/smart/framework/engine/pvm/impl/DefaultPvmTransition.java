package com.alibaba.smart.framework.engine.pvm.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.Transition;
import com.alibaba.smart.framework.engine.behavior.TransitionBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DefaultPvmTransition extends AbstractPvmElement<Transition> implements PvmTransition {

    private PvmActivity source;
    private PvmActivity target;

    private TransitionBehavior behavior;

    @Override
    public boolean match(ExecutionContext context) {
        Transition model = this.getModel();

        return this.behavior.match(context,model.getConditionExpression());
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public String toString() {
        return " " + getModel();
    }
}
