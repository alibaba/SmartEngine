//package com.alibaba.smart.framework.engine.modules.compatible.activiti.provider.multi.instance;
//
//import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
//import com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance.AbortCheckPerformable;
//import com.alibaba.smart.framework.engine.provider.Performer;
//import com.alibaba.smart.framework.engine.provider.factory.PerformerProviderFactory;
//import com.alibaba.smart.framework.engine.pvm.PvmElement;
//
///**
// * @author ettear
// * Created by ettear on 15/10/2017.
// */
//public class AbortCheckProviderFactory
//    implements PerformerProviderFactory<AbortCheckPerformable> {
//    private ExtensionPointRegistry extensionPointRegistry;
//
//    public AbortCheckProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
//        this.extensionPointRegistry = extensionPointRegistry;
//    }
//
//    @Override
//    public Performer createPerformer(PvmElement pvmElement, AbortCheckPerformable performable) {
//        return new AbortCheckProvider(this.extensionPointRegistry);
//    }
//
//    @Override
//    public Class<AbortCheckPerformable> getModelType() {
//        return AbortCheckPerformable.class;
//    }
//}
