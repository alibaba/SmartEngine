package com.alibaba.smart.framework.engine.modules.bpmn.provider.process;

import com.alibaba.smart.framework.engine.common.expression.ExpressionPerformer;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.provider.Performer;
import com.alibaba.smart.framework.engine.provider.TransitionBehavior;
import com.alibaba.smart.framework.engine.provider.impl.AbstractTransitionBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

@ExtensionBinding(type = ExtensionConstant.ACTIVITY_BEHAVIOR, bindingTo = TransitionBehavior.class)

public class SequenceFlowBehavior extends AbstractTransitionBehavior<SequenceFlow> {

    public SequenceFlowBehavior() {
        super();
    }

    private Performer matchPerformer;
    public SequenceFlowBehavior(ExtensionPointRegistry extensionPointRegistry, PvmTransition runtimeTransition) {
        super(extensionPointRegistry,runtimeTransition);

        //Performable matchPerformable = getModel().getConditionExpression();
        //if(null!=matchPerformable) {
        //    ProviderFactoryExtensionPoint providerFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(
        //        ProviderFactoryExtensionPoint.class);
        //
        //    PerformerProviderFactory matchPerformerProviderFactory
        //        = (PerformerProviderFactory)providerFactoryExtensionPoint
        //        .getProviderFactory(matchPerformable.getClass());
        //    this.matchPerformer = matchPerformerProviderFactory.createPerformer(null, matchPerformable);
        //}
    }

    @Override
    public boolean match(ExecutionContext context, ConditionExpression conditionExpression) {

        if (null != conditionExpression) {
            String type =  conditionExpression.getExpressionType();

            if (null != type) {
                int expressionTypeSplitIndex = type.indexOf(":");
                if (expressionTypeSplitIndex >= 0) {
                    type = type.substring(expressionTypeSplitIndex + 1);
                }
            } else {
                type = "mvel";
            }

            Object eval = ExpressionPerformer.eval(type,
                conditionExpression.getExpressionContent(), context);
            return (Boolean)eval;
        }else{
            throw new EngineException("SHOULD NOT TOUCH THIS");
        }
    }
}
