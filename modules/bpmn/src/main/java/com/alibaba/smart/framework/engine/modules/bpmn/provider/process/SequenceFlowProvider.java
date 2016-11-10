package com.alibaba.smart.framework.engine.modules.bpmn.provider.process;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.modules.mvel.MvelInvoker;
import com.alibaba.smart.framework.engine.provider.TransitionProvider;
import com.alibaba.smart.framework.engine.provider.impl.AbstractTransitionProvider;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

public class SequenceFlowProvider extends AbstractTransitionProvider<SequenceFlow> implements TransitionProvider<SequenceFlow> {

//    private ExtensionPointRegistry        extensionPointRegistry;
//    private HandlerProviderExtensionPoint handlerProviderExtensionPoint;

    public SequenceFlowProvider(ExtensionPointRegistry extensionPointRegistry, PvmTransition runtimeTransition) {
        super(runtimeTransition);
//        this.extensionPointRegistry = extensionPointRegistry;
//        this.handlerProviderExtensionPoint = this.extensionPointRegistry.getExtensionPoint(HandlerProviderExtensionPoint.class);
    }

    @Override
    protected Invoker createHitInvoker() {
        SequenceFlow sequenceFlow = (SequenceFlow) this.getRuntimeTransition().getModel();
        ConditionExpression conditionExpression = sequenceFlow.getConditionExpression();

        if (null != conditionExpression) {
            if ("mvel".equals(conditionExpression.getExpressionType())) {
                return new MvelInvoker(conditionExpression);
            }else{
                throw new EngineException("unsupported condition expression type:"+conditionExpression.getExpressionType());
            }

            // if (null != conditionExpression && null != conditionExpression.getHandler()) {
            // return this.handlerProviderExtensionPoint.createInvoker(conditionExpression.getHandler());
            // }
           
        } else {
            // 不存在条件表达式
            return null;
        }

    }

    @Override
    protected Invoker createExecuteInvoker() {
//        SequenceFlow sequenceFlow = (SequenceFlow) this.getRuntimeTransition().getModel();
        return null;
        // Handler handler = sequenceFlow.getHandler();
        // if (null != handler) {
        // return this.handlerProviderExtensionPoint.createInvoker(handler);
        // } else {
        // // 不存在条件表达式
        // return null;
        // }
    }
}
