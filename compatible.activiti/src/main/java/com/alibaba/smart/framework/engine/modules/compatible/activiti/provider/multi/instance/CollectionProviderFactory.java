package com.alibaba.smart.framework.engine.modules.compatible.activiti.provider.multi.instance;

import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.multi.instance.LoopCollectionProviderFactory;
import com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance.Collection;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class CollectionProviderFactory implements LoopCollectionProviderFactory<Collection> {
    private ExtensionPointRegistry extensionPointRegistry;

    public CollectionProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }
    @Override
    public CollectionProvider createProvider(Collection collection) {
        return new CollectionProvider(this.extensionPointRegistry,collection);
    }

    @Override
    public Class<Collection> getModelType() {
        return Collection.class;
    }
}
