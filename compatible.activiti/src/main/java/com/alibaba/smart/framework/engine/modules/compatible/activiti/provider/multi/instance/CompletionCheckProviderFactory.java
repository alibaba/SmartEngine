package com.alibaba.smart.framework.engine.modules.compatible.activiti.provider.multi.instance;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance
    .CompletionCheckPerformable;
import com.alibaba.smart.framework.engine.provider.Performer;
import com.alibaba.smart.framework.engine.provider.factory.PerformerProviderFactory;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class CompletionCheckProviderFactory
    implements PerformerProviderFactory<CompletionCheckPerformable> {
    private ExtensionPointRegistry extensionPointRegistry;

    public CompletionCheckProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public Performer createPerformer(CompletionCheckPerformable performable) {
        return new CompletionCheckProvider(this.extensionPointRegistry);
    }

    @Override
    public Class<CompletionCheckPerformable> getModelType() {
        return CompletionCheckPerformable.class;
    }
}
