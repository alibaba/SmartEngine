package com.alibaba.smart.framework.engine.modules.bpmn.provider.process;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.provider.TransitionBehavior;
import com.alibaba.smart.framework.engine.provider.impl.AbstractTransition;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import org.mvel2.MVEL;

public class SequenceFlow extends AbstractTransition<com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow> implements TransitionBehavior<com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow> {


    public SequenceFlow(ExtensionPointRegistry extensionPointRegistry, PvmTransition runtimeTransition) {
        super(runtimeTransition);
    }



    @Override
    public boolean execute(PvmTransition pvmTransition, ExecutionContext context) {
        com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow sequenceFlow = (com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow) pvmTransition.getModel();
        ConditionExpression conditionExpression = sequenceFlow.getConditionExpression();

        if (null != conditionExpression) {
            //TODO SUPPORT OTHER ,TODO  预编译
            if ("mvel".equals(conditionExpression.getExpressionType())) {
                Object result = MVEL.eval(conditionExpression.getExpressionContent(), context.getRequest());
                return (Boolean) result;
            } else {
                throw new EngineException("unsupported condition expression type:" + conditionExpression.getExpressionType());
            }
        }
        return true;
    }
}
