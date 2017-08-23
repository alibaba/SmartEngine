package com.alibaba.smart.framework.engine.provider.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.TaskInstanceFactory;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public abstract class AbstractActivityBehavior<T extends Activity> implements ActivityBehavior {

    private PvmActivity runtimeActivity;

    protected ExtensionPointRegistry extensionPointRegistry;

    protected ProcessInstanceFactory processInstanceFactory;
    protected ExecutionInstanceFactory executionInstanceFactory;
    protected ActivityInstanceFactory activityInstanceFactory;
    protected TaskInstanceFactory taskInstanceFactory;

    public AbstractActivityBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity pvmActivity) {
        this.runtimeActivity = pvmActivity;
        this.extensionPointRegistry = extensionPointRegistry;
        this.processInstanceFactory = extensionPointRegistry.getExtensionPoint(ProcessInstanceFactory.class);
        this.executionInstanceFactory = extensionPointRegistry.getExtensionPoint(ExecutionInstanceFactory.class);
        this.activityInstanceFactory = extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);
        this.taskInstanceFactory = extensionPointRegistry.getExtensionPoint(TaskInstanceFactory.class);
    }

    protected ExtensionPointRegistry getExtensionPointRegistry() {
        return extensionPointRegistry;
    }


    protected PvmActivity getRuntimeActivity() {
        return runtimeActivity;
    }

    @Override
    public boolean enter(ExecutionContext context) {
        return false;
    }

    @Override
    public boolean execute(ExecutionContext context) {
        return false;
    }

    @Override
    public void leave(ExecutionContext context) {

    }

    protected T getModel() {
        return (T)this.runtimeActivity.getModel();
    }
}
