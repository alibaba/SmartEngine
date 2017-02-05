package com.alibaba.smart.framework.engine.modules.bpmn.provider.process;

import com.alibaba.smart.framework.engine.common.expression.evaluator.ExpressionEvaluator;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.util.ClassLoaderUtil;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.provider.TransitionBehavior;
import com.alibaba.smart.framework.engine.provider.impl.AbstractTransition;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

public class SequenceFlowBehavior extends AbstractTransition<com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow> implements TransitionBehavior<com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow> {

    private static final String PACKAGE_NAME = "com.alibaba.smart.framework.engine.common.expression.evaluator.";

    public SequenceFlowBehavior(ExtensionPointRegistry extensionPointRegistry, PvmTransition runtimeTransition) {
        super(runtimeTransition);
    }



    @Override
    public boolean execute(PvmTransition pvmTransition, ExecutionContext context) {
        SequenceFlow sequenceFlow =  (SequenceFlow)pvmTransition.getModel();
        ConditionExpression conditionExpression = sequenceFlow.getConditionExpression();

        if (null != conditionExpression) {
            String expressionType = conditionExpression.getExpressionType();
            String firstCharToUpperCase  = Character.toUpperCase(expressionType.charAt(0)) + expressionType.substring(1);
            String className = PACKAGE_NAME +firstCharToUpperCase+"ExpressionEvaluator";
            ExpressionEvaluator expressionEvaluator = (ExpressionEvaluator) ClassLoaderUtil.createOrGetInstanceWithASM(className);

            Object result = expressionEvaluator.eval(conditionExpression.getExpressionContent(), context.getRequest());
            return (Boolean) result;

        }
        return true;
    }
}
