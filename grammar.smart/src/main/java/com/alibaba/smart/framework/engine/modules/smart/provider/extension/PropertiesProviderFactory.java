//package com.alibaba.smart.framework.engine.modules.smart.provider.extension;
//
//import java.util.List;
//
//import com.alibaba.smart.framework.engine.exception.EngineException;
//import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
//import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.Properties;
//import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.Value;
//import com.alibaba.smart.framework.engine.provider.Invoker;
//import com.alibaba.smart.framework.engine.provider.ProviderFactoryExtensionPoint;
//import com.alibaba.smart.framework.engine.provider.factory.InvokerProviderFactory;
//
//public class PropertiesProviderFactory implements
//    InvokerProviderFactory<Properties> {
//
//    private ExtensionPointRegistry extensionPointRegistry;
//    private ProviderFactoryExtensionPoint providerFactoryExtensionPoint;
//
//    public PropertiesProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
//        this.extensionPointRegistry = extensionPointRegistry;
//        this.providerFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(
//            ProviderFactoryExtensionPoint.class);
//    }
//
//    @Override
//    public Invoker createInvoker(Properties properties) {
//
//        //extensionList 这个里面只可能是Value 类型
//        List<Value> extensionList   =  properties.getExtensionList();
//
//        if(null != extensionList){
//            return InvokerUtil.createMultiValueInvoker(extensionPointRegistry,extensionList);
//
//        }
//
//        throw  new EngineException("Unsupported Type:"+properties.getExtensionList());
//    }
//
//
//    @Override
//    public Class<Properties> getModelType() {
//        return Properties.class;
//    }
//
//}
