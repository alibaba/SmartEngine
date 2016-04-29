package com.alibaba.smart.framework.engine.modules.bpmn.provider.process;

import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensibility.HandlerProviderExtensionPoint;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.AbstractBpmnActivity;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.assembly.Handler;
import com.alibaba.smart.framework.engine.assembly.Script;
import com.alibaba.smart.framework.engine.provider.TransitionProvider;
import com.alibaba.smart.framework.engine.provider.impl.AbstractTransitionProvider;
import com.alibaba.smart.framework.engine.runtime.RuntimeTransition;
import com.alibaba.smart.framework.engine.script.ScriptCompilerExtensionPoint;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.process.behavior.DefaultInvoker;

public class SequenceFlowProvider extends AbstractTransitionProvider<SequenceFlow> implements TransitionProvider<SequenceFlow> {

    private ExtensionPointRegistry        extensionPointRegistry;
    private HandlerProviderExtensionPoint handlerProviderExtensionPoint;


    public SequenceFlowProvider(ExtensionPointRegistry extensionPointRegistry,RuntimeTransition runtimeTransition) {
       super(runtimeTransition);
        this.extensionPointRegistry=extensionPointRegistry;
        this.handlerProviderExtensionPoint=this.extensionPointRegistry.getExtensionPoint(HandlerProviderExtensionPoint.class);
    }

    @Override
    protected Invoker createHitInvoker() {
        SequenceFlow sequenceFlow=(SequenceFlow)this.getRuntimeTransition().getModel();
        ConditionExpression conditionExpression=sequenceFlow.getConditionExpression();
        if(null!=conditionExpression && null!=conditionExpression.getHandler()) {
            return this.handlerProviderExtensionPoint.createInvoker(conditionExpression.getHandler());
        }else{
            //不存在条件表达式
            return null;
        }
    }

    @Override
    protected Invoker createExecuteInvoker() {
        SequenceFlow sequenceFlow=(SequenceFlow)this.getRuntimeTransition().getModel();
        Handler handler=sequenceFlow.getHandler();
        if(null!=handler) {
            return this.handlerProviderExtensionPoint.createInvoker(handler);
        }else{
            //不存在条件表达式
            return null;
        }
    }
}
