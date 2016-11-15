//package com.alibaba.smart.framework.engine.modules.bpmn.provider.process;
//
//import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.Process;
//import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
//import com.alibaba.smart.framework.engine.pvm.PvmActivity;
//
//public class ProcessProviderFactory implements ActivityProviderFactory<Process> {
//
//    @Override
//    public ProcessBehaviorProvider createActivityProvider(PvmActivity activity) {
//        return new ProcessBehaviorProvider(activity);
//    }
//
//    @Override
//    public Class<Process> getModelType() {
//        return Process.class;
//    }
//}
