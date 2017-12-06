package com.alibaba.smart.framework.engine.modules.extensions.transaction.provider;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.action.SingleTaskBehavior;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.node.SingleTask;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * @author Leo.yy   Created on 2017/8/2.
 * @description
 * @see
 */
public class SingleTaskProviderFactory implements ActivityProviderFactory<SingleTask> {


    private ExtensionPointRegistry extensionPointRegistry;

    public SingleTaskProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public ActivityBehavior<SingleTask> createActivityProvider(PvmActivity activity) {
        return new SingleTaskBehavior(extensionPointRegistry, activity);
    }

    @Override
    public Class<SingleTask> getModelType() {
        return SingleTask.class;
    }

}
