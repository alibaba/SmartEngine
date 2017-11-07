package com.alibaba.smart.framework.engine.modules.bpmn.provider.multi.instance;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.LoopDataInputRef;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class LoopDataInputRefProviderFactory implements LoopCollectionProviderFactory<LoopDataInputRef> {
    private ExtensionPointRegistry extensionPointRegistry;

    public LoopDataInputRefProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public LoopDataInputRefProvider createProvider(LoopDataInputRef loopDataInputRef) {
        return new LoopDataInputRefProvider(this.extensionPointRegistry, loopDataInputRef);
    }

    @Override
    public Class<LoopDataInputRef> getModelType() {
        return LoopDataInputRef.class;
    }
}
