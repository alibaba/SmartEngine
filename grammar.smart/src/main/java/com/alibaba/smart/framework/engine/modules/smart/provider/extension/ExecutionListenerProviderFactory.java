//package com.alibaba.smart.framework.engine.modules.smart.provider.extension;
//
//import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
//import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.ExecutionListener;
//import com.alibaba.smart.framework.engine.provider.Invoker;
//import com.alibaba.smart.framework.engine.provider.ProviderFactoryExtensionPoint;
//import com.alibaba.smart.framework.engine.provider.factory.InvokerProviderFactory;
//
///**
// * @author ettear
// * Created by ettear on 06/08/2017.
// */
//public class ExecutionListenerProviderFactory implements
//    InvokerProviderFactory<ExecutionListener> {
//
//    private ExtensionPointRegistry extensionPointRegistry;
//    private ProviderFactoryExtensionPoint providerFactoryExtensionPoint;
//
//    public ExecutionListenerProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
//        this.extensionPointRegistry = extensionPointRegistry;
//        this.providerFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(
//            ProviderFactoryExtensionPoint.class);
//    }
//
//    @Override
//    public Invoker createInvoker(ExecutionListener executionListener) {
//        return InvokerUtil.createExecutionListenerInvoker( extensionPointRegistry, providerFactoryExtensionPoint,executionListener);
//    }
//
//
//
//    @Override
//    public Class<ExecutionListener> getModelType() {
//        return ExecutionListener.class;
//    }
//
//}
