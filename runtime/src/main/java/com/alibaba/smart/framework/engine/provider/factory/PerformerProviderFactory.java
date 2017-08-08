package com.alibaba.smart.framework.engine.provider.factory;

import com.alibaba.smart.framework.engine.model.assembly.Performable;
import com.alibaba.smart.framework.engine.provider.Performer;

/**
 * @author ettear
 * Created by ettear on 02/08/2017.
 */
public interface PerformerProviderFactory<M extends Performable> extends ProviderFactory<M> {
    /**
     * Create Performer
     *
     * @param performable Performable
     * @return Performer
     */
    Performer createPerformer(M performable);
}
