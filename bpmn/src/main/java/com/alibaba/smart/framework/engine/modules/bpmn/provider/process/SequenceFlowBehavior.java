package com.alibaba.smart.framework.engine.modules.bpmn.provider.process;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.Process;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.provider.Performer;
import com.alibaba.smart.framework.engine.provider.TransitionBehavior;
import com.alibaba.smart.framework.engine.provider.impl.AbstractTransitionBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

@ExtensionBinding(type = ExtensionConstant.ACTIVITY_BEHAVIOR,binding = TransitionBehavior.class)

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
    public boolean match(ExecutionContext context) {
        if (null != this.matchPerformer) {
            return (Boolean)this.matchPerformer.perform(context);
        }
        return true;
    }
}
