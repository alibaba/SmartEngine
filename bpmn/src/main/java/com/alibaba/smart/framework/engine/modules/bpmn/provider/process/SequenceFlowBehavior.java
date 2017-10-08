package com.alibaba.smart.framework.engine.modules.bpmn.provider.process;

import com.alibaba.smart.framework.engine.common.expression.evaluator.ExpressionEvaluator;
import com.alibaba.smart.framework.engine.configuration.InstanceAccessor;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.provider.impl.AbstractTransitionBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

public class SequenceFlowBehavior extends AbstractTransitionBehavior<SequenceFlow> {

    private static final String PACKAGE_NAME = "com.alibaba.smart.framework.engine.common.expression.evaluator.";

    public SequenceFlowBehavior(ExtensionPointRegistry extensionPointRegistry, PvmTransition runtimeTransition) {
        super(extensionPointRegistry,runtimeTransition);
    }



    @Override
    public boolean match(ExecutionContext context) {
        ConditionExpression conditionExpression = getModel().getConditionExpression();

        if (null != conditionExpression) {
            String expressionType = conditionExpression.getExpressionType();
            String firstCharToUpperCase  = Character.toUpperCase(expressionType.charAt(0)) + expressionType.substring(1);
            String className = PACKAGE_NAME +firstCharToUpperCase+"ExpressionEvaluator";
            InstanceAccessor instanceAccessor = context.getProcessEngineConfiguration()
                .getInstanceAccessor();
            ExpressionEvaluator expressionEvaluator = (ExpressionEvaluator) instanceAccessor.access(className);

            Object result = expressionEvaluator.eval(conditionExpression.getExpressionContent(), context.getRequest());
            return (Boolean) result;

        }
        return true;
    }
}
