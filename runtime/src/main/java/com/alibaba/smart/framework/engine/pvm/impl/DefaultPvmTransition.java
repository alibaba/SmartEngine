package com.alibaba.smart.framework.engine.pvm.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
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
public class DefaultPvmTransition extends AbstractPvmElement<Transition> implements PvmTransition {

    private PvmActivity source;
    private PvmActivity target;

    private TransitionBehavior behavior;

    public DefaultPvmTransition(ExtensionPointRegistry extensionPointRegistry){
        super(extensionPointRegistry);
    }

    @Override
    public boolean match(ExecutionContext context) {
        return this.behavior.match(context);
    }

    @Override
    public void execute(ExecutionContext context) {
        this.invoke(PvmEventConstant.TRANSITION_EXECUTE.name(), context);

        PvmActivity targetPvmActivity = this.getTarget();
        context.setSourcePvmActivity(this.getSource());
        //重要: 执行当前节点,会触发当前节点的行为执行
        targetPvmActivity.enter(context);
    }

    @Override
    protected Object invokeBehavior(String event, ExecutionContext context) {
        if (PvmEventConstant.TRANSITION_EXECUTE.name().equals(event)) {
             return this.behavior.execute(context);
        }
        // TUNE XXX
        return  null;
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
