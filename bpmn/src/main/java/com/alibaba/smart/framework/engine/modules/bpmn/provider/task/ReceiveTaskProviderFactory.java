//package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;
//
//import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
//import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ReceiveTask;
//import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
//import com.alibaba.smart.framework.engine.pvm.PvmActivity;
//
//public class ReceiveTaskProviderFactory implements ActivityProviderFactory<ReceiveTask> {
//
//    private ExtensionPointRegistry extensionPointRegistry;
//
//    public ReceiveTaskProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
//        this.extensionPointRegistry = extensionPointRegistry;
//    }
//
//    @Override
//    public ReceiveTaskBehavior createActivityProvider(PvmActivity activity) {
//        return new ReceiveTaskBehavior(this.extensionPointRegistry, activity);
//    }
//
//    @Override
//    public Class<ReceiveTask> getModelType() {
//        return ReceiveTask.class;
//    }
//}
