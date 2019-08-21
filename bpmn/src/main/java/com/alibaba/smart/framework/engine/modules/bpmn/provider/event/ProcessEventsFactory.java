//package com.alibaba.smart.framework.engine.modules.bpmn.provider.event;
//
//import ExtensionPointRegistry;
//import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.ProcessEvents;
//import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
//import com.alibaba.smart.framework.engine.pvm.PvmActivity;
//
///**
// * Created by dongdongzdd on 16/9/20.
// */
//public class ProcessEventsFactory implements ActivityProviderFactory<ProcessEvents> {
//
//    private ExtensionPointRegistry extensionPointRegistry;
//
//    public ProcessEventsFactory(ExtensionPointRegistry extensionPointRegistry) {
//        this.extensionPointRegistry = extensionPointRegistry;
//    }
//
//    @Override
//    public ProcessEventsProvider createActivityProvider(PvmActivity activity) {
//        return new ProcessEventsProvider(this.extensionPointRegistry, activity);
//    }
//
//    @Override
//    public Class<ProcessEvents> getModelType() {
//        return ProcessEvents.class;
//    }
//}
