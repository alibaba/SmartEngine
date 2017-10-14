package com.alibaba.smart.framework.engine.provider.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.TaskInstanceFactory;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public abstract class AbstractActivityBehavior<T extends Activity> implements ActivityBehavior {

    private PvmActivity pvmActivity;

    protected ExtensionPointRegistry extensionPointRegistry;

    protected ProcessInstanceFactory processInstanceFactory;
    protected ExecutionInstanceFactory executionInstanceFactory;
    protected ActivityInstanceFactory activityInstanceFactory;
    protected TaskInstanceFactory taskInstanceFactory;

    public AbstractActivityBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity pvmActivity) {
        this.pvmActivity = pvmActivity;
        this.extensionPointRegistry = extensionPointRegistry;
        this.processInstanceFactory = extensionPointRegistry.getExtensionPoint(ProcessInstanceFactory.class);
        this.executionInstanceFactory = extensionPointRegistry.getExtensionPoint(ExecutionInstanceFactory.class);
        this.activityInstanceFactory = extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);
        this.taskInstanceFactory = extensionPointRegistry.getExtensionPoint(TaskInstanceFactory.class);
    }

    protected ExtensionPointRegistry getExtensionPointRegistry() {
        return extensionPointRegistry;
    }


    protected PvmActivity getPvmActivity() {
        return pvmActivity;
    }

    @Override
    public boolean enter(ExecutionContext context) {
        return false;
    }

    protected void beforeExecute(ExecutionContext context) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        ExecutionInstanceStorage executionInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);

        ExecutionInstance executionInstance=context.getExecutionInstance();

        //只负责完成当前executionInstance的状态更新,此时产生了 DB 写.
        MarkDoneUtil.markDone(executionInstance,executionInstanceStorage);
    }


    @Override
    public boolean execute(ExecutionContext context) {
        //TODO 重新看下。
        beforeExecute(context);
        return false;
    }

    @Override
    public void leave(ExecutionContext context) {

    }

    protected T getModel() {
        return (T)this.pvmActivity.getModel();
    }
}
