package com.alibaba.smart.framework.engine.modules.bpmn.provider.process;

import com.alibaba.smart.framework.engine.common.expression.ExpressionPerformer;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.Performable;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.provider.Performer;
import com.alibaba.smart.framework.engine.provider.ProviderFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.factory.PerformerProviderFactory;
import com.alibaba.smart.framework.engine.provider.impl.AbstractTransitionBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

public class SequenceFlowBehavior extends AbstractTransitionBehavior<SequenceFlow> {

    private Performer matchPerformer;
    public SequenceFlowBehavior(ExtensionPointRegistry extensionPointRegistry, PvmTransition runtimeTransition) {
        super(extensionPointRegistry,runtimeTransition);

        Performable matchPerformable = getModel().getConditionExpression();
        if(null!=matchPerformable) {
            ProviderFactoryExtensionPoint providerFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(
                ProviderFactoryExtensionPoint.class);

            PerformerProviderFactory matchPerformerProviderFactory
                = (PerformerProviderFactory)providerFactoryExtensionPoint
                .getProviderFactory(matchPerformable.getClass());
            this.matchPerformer = matchPerformerProviderFactory.createPerformer(matchPerformable);
        }
    }

    @Override
    public boolean match(ExecutionContext context) {
        if (null != this.matchPerformer) {
            return (Boolean)this.matchPerformer.perform(context);
        }
        return true;
    }
}
