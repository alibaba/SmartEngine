package com.alibaba.smart.framework.engine.modules.compatible.activiti.provider.multi.instance;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance
    .CompletionCheckPreparePerformable;
import com.alibaba.smart.framework.engine.provider.Performer;
import com.alibaba.smart.framework.engine.provider.factory.PerformerProviderFactory;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class CompletionCheckPrepareProviderFactory
    implements PerformerProviderFactory<CompletionCheckPreparePerformable> {
    private ExtensionPointRegistry extensionPointRegistry;

    public CompletionCheckPrepareProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public Performer createPerformer(CompletionCheckPreparePerformable performable) {
        return new CompletionCheckPrepareProvider(this.extensionPointRegistry);
    }

    @Override
    public Class<CompletionCheckPreparePerformable> getModelType() {
        return CompletionCheckPreparePerformable.class;
    }
}
