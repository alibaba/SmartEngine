package com.alibaba.smart.framework.engine.modules.smart.provider.process;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.smart.assembly.process.SmartSequenceFlow;
import com.alibaba.smart.framework.engine.provider.impl.AbstractTransitionBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * @author ettear
 * Created by ettear on 07/08/2017.
 */
public class SequenceFlowBehavior extends AbstractTransitionBehavior<SmartSequenceFlow> {
    public SequenceFlowBehavior(ExtensionPointRegistry extensionPointRegistry,PvmTransition runtimeTransition) {
        super(extensionPointRegistry,runtimeTransition);
    }

    @Override
    public boolean match(ExecutionContext context, ConditionExpression conditionExpression) {
        return true;
    }
}
