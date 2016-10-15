package com.alibaba.smart.framework.engine.modules.bpmn.provider.process;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.AbstractBpmnActivity;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehaviorProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * Created by ettear on 16-4-29.
 */
public   class AbstractBpmnActivityBehaviorProvider<M extends AbstractBpmnActivity> extends AbstractActivityBehaviorProvider<M> {

    private ExtensionPointRegistry        extensionPointRegistry;
//    private HandlerProviderExtensionPoint handlerProviderExtensionPoint;

    public AbstractBpmnActivityBehaviorProvider(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(runtimeActivity);
        this.extensionPointRegistry = extensionPointRegistry;
//        this.handlerProviderExtensionPoint = this.extensionPointRegistry.getExtensionPoint(HandlerProviderExtensionPoint.class);
    }

    @Override
    protected Invoker createExecuteInvoker() {
//        AbstractBpmnActivity bpmnActivity = (AbstractBpmnActivity) this.getRuntimeActivity().getModel();
//        Handler handler = bpmnActivity.getHandler();
        return null;
        //		FIXME
//        if (null != handler) {
//            return this.handlerProviderExtensionPoint.createInvoker(handler);
//        } else {
//            // 不存在条件表达式
//            return null;
//        }
    }

    protected ExtensionPointRegistry getExtensionPointRegistry() {
        return extensionPointRegistry;
    }
}
