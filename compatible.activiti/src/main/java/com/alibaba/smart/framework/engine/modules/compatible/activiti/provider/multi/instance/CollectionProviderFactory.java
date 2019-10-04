//package com.alibaba.smart.framework.engine.modules.compatible.activiti.provider.multi.instance;
//
//import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
//import com.alibaba.smart.framework.engine.modules.bpmn.provider.multi.instance.LoopCollectionProviderFactory;
//import com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance.ActivitiCollection;
//
///**
// * @author ettear
// * Created by ettear on 15/10/2017.
// */
//public class CollectionProviderFactory implements LoopCollectionProviderFactory<ActivitiCollection> {
//    private ExtensionPointRegistry extensionPointRegistry;
//
//    public CollectionProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
//        this.extensionPointRegistry = extensionPointRegistry;
//    }
//    @Override
//    public CollectionProvider createProvider(ActivitiCollection activitiCollection) {
//        return new CollectionProvider(this.extensionPointRegistry, activitiCollection);
//    }
//
//    @Override
//    public Class<ActivitiCollection> getModelType() {
//        return ActivitiCollection.class;
//    }
//}
