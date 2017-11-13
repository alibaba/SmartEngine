package com.alibaba.smart.framework.engine.modules.bpmn.provider.multi.instance;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.LoopCollection;
import com.alibaba.smart.framework.engine.provider.factory.ProviderFactory;

/**
 * @author ettear
 * Created by ettear on 02/08/2017.
 */
public interface LoopCollectionProviderFactory<M extends LoopCollection> extends ProviderFactory<M> {
    /**
     * Create Performer
     *
     * @param loopCollection LoopCollection
     * @return Performer
     */
    LoopCollectionProvider createProvider(M loopCollection);
}
