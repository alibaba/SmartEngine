package com.alibaba.smart.framework.engine.bpmn.behavior.process;

import com.alibaba.smart.framework.engine.behavior.TransitionBehavior;
import com.alibaba.smart.framework.engine.behavior.base.AbstractTransitionBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.common.expression.ExpressionUtil;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.ConditionExpression;
import com.alibaba.smart.framework.engine.model.assembly.Transition;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = TransitionBehavior.class)
public class SequenceFlowBehavior extends AbstractTransitionBehavior<SequenceFlow> {

    @Override
    public boolean match(ExecutionContext context, Transition transition) {

        ConditionExpression conditionExpression = transition.getConditionExpression();

        if (null != conditionExpression) {
            return ExpressionUtil.eval(context, conditionExpression);
        }else{
            throw new EngineException("Should config condition expression for ExclusiveGateway,the sequenceFlow is "+transition);
        }
    }
}
