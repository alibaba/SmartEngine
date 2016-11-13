package com.alibaba.smart.framework.engine.modules.bpmn.provider.process;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.TaskInstanceFactory;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.AbstractBpmnActivity;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehaviorProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * Created by ettear on 16-4-29.
 */
public abstract   class AbstractBpmnActivityBehaviorProvider<M extends AbstractBpmnActivity> extends AbstractActivityBehaviorProvider<M> {

    private ExtensionPointRegistry        extensionPointRegistry;

    protected ProcessInstanceFactory processInstanceFactory;
    protected ExecutionInstanceFactory executionInstanceFactory;
    protected ActivityInstanceFactory activityInstanceFactory;
    protected TaskInstanceFactory taskInstanceFactory;

    public AbstractBpmnActivityBehaviorProvider(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(runtimeActivity);
        this.extensionPointRegistry = extensionPointRegistry;
        this.processInstanceFactory= extensionPointRegistry.getExtensionPoint(ProcessInstanceFactory.class) ;
        this.executionInstanceFactory= extensionPointRegistry.getExtensionPoint(ExecutionInstanceFactory.class) ;
        this.activityInstanceFactory = extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);
        this.taskInstanceFactory = extensionPointRegistry.getExtensionPoint(TaskInstanceFactory.class);
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
