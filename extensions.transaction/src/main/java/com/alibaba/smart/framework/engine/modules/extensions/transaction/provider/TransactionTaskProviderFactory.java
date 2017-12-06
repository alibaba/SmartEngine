package com.alibaba.smart.framework.engine.modules.extensions.transaction.provider;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.behavior.TransactionTaskBehavior;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.node.TransactionTask;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * @author Leo.yy   Created on 17/7/31.
 * @description
 * @see
 */
public class TransactionTaskProviderFactory implements ActivityProviderFactory<TransactionTask> {

    private ExtensionPointRegistry extensionPointRegistry;

    public TransactionTaskProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public ActivityBehavior<TransactionTask> createActivityProvider(PvmActivity pvmActivity) {
        return new TransactionTaskBehavior(extensionPointRegistry, pvmActivity);
    }

    @Override
    public Class<TransactionTask> getModelType() {
        return TransactionTask.class;
    }
}
