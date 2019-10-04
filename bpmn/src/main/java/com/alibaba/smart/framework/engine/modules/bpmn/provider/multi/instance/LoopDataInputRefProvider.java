//package com.alibaba.smart.framework.engine.modules.bpmn.provider.multi.instance;
//
//import java.util.Collection;
//
//import com.alibaba.smart.framework.engine.context.ExecutionContext;
//import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
//import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.LoopDataInputRef;
//import com.alibaba.smart.framework.engine.pvm.PvmActivity;
//
///**
// * @author ettear
// * Created by ettear on 15/10/2017.
// */
//public class LoopDataInputRefProvider implements LoopCollectionProvider {
//    private ExtensionPointRegistry extensionPointRegistry;
//    private LoopDataInputRef loopDataInputRef;
//
//    LoopDataInputRefProvider(ExtensionPointRegistry extensionPointRegistry, LoopDataInputRef loopDataInputRef) {
//        this.extensionPointRegistry = extensionPointRegistry;
//        this.loopDataInputRef = loopDataInputRef;
//
//    }
//
//    @Override
//    public Collection<Object> getCollection(ExecutionContext context, PvmActivity activity) {
//        if (null != this.loopDataInputRef) {
//            Object collection = context.getRequest().get(this.loopDataInputRef.getReference());
//            if (null == collection || !(collection instanceof Collection)) {
//                collection = context.getPrivateContext().get(this.loopDataInputRef.getReference());
//            }
//
//            if (null != collection && collection instanceof Collection) {
//                return (Collection<Object>)collection;
//            }
//        }
//        return null;
//    }
//}
