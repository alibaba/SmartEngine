package com.alibaba.smart.framework.engine.modules.bpmn.provider.multi.instance;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.LoopCardinality;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class LoopCardinalityProviderFactory implements LoopCollectionProviderFactory<LoopCardinality> {
    private ExtensionPointRegistry extensionPointRegistry;

    public LoopCardinalityProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public LoopCardinalityProvider createProvider(LoopCardinality collection) {
        return new LoopCardinalityProvider(this.extensionPointRegistry, collection);
    }

    @Override
    public Class<LoopCardinality> getModelType() {
        return LoopCardinality.class;
    }
}
