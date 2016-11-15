//package com.alibaba.smart.framework.engine.modules.bpmn.provider.event;
//
//import com.alibaba.smart.framework.engine.context.ExecutionContext;
//import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
//import com.alibaba.smart.framework.engine.invocation.Invoker;
//import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.ProcessEvents;
//import com.alibaba.smart.framework.engine.modules.bpmn.provider.process.AbstractBpmnActivityBehaviorProvider;
//import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
//import com.alibaba.smart.framework.engine.pvm.PvmActivity;
//
///**
// * Created by dongdongzdd on 16/9/20.
// */
//public class ProcessEventsProvider extends AbstractBpmnActivityBehaviorProvider<ProcessEvents> implements ActivityBehavior<ProcessEvents> {
//
//    public ProcessEventsProvider(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
//        super(extensionPointRegistry, runtimeActivity);
//    }
//
//
//    @Override
//    protected Invoker createEventInvoker(String event) {
//        return new ProcessEventInvoker(getRuntimeActivity());
//    }
//
//    @Override
//    public void execute(PvmActivity runtimeActivity, ExecutionContext context) {
//
//    }
//}
