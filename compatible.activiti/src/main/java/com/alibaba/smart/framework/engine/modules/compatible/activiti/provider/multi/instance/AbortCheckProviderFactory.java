package com.alibaba.smart.framework.engine.modules.compatible.activiti.provider.multi.instance;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance.AbortCheckPerformable;
import com.alibaba.smart.framework.engine.provider.Performer;
import com.alibaba.smart.framework.engine.provider.factory.PerformerProviderFactory;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class AbortCheckProviderFactory
    implements PerformerProviderFactory<AbortCheckPerformable> {
    private ExtensionPointRegistry extensionPointRegistry;

    public AbortCheckProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public Performer createPerformer(AbortCheckPerformable performable) {
        return new AbortCheckProvider(this.extensionPointRegistry);
    }

    @Override
    public Class<AbortCheckPerformable> getModelType() {
        return AbortCheckPerformable.class;
    }
}
