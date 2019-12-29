package com.alibaba.smart.framework.engine.modules.bpmn.provider.process;

import com.alibaba.smart.framework.engine.common.expression.ExpressionPerformer;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.provider.TransitionBehavior;
import com.alibaba.smart.framework.engine.provider.impl.AbstractTransitionBehavior;

@ExtensionBinding(type = ExtensionConstant.ACTIVITY_BEHAVIOR, bindingTo = TransitionBehavior.class)
public class SequenceFlowBehavior extends AbstractTransitionBehavior<SequenceFlow> {

    @Override
    public boolean match(ExecutionContext context, ConditionExpression conditionExpression) {

        if (null != conditionExpression) {
            return ExpressionPerformer.eval(context, conditionExpression);
        }else{
            throw new EngineException("SHOULD NOT TOUCH THIS");
        }
    }
}
