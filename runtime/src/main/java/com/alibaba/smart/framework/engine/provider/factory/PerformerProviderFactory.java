package com.alibaba.smart.framework.engine.provider.factory;

import com.alibaba.smart.framework.engine.model.assembly.Performable;
import com.alibaba.smart.framework.engine.provider.Performer;
import com.alibaba.smart.framework.engine.pvm.PvmElement;

/**
 * @author ettear
 * Created by ettear on 02/08/2017.
 */
public interface PerformerProviderFactory<M extends Performable> extends ProviderFactory<M> {
    /**
     * Create Performer
     *
     *
     * @param pvmElement
     * @param performable Performable
     * @return Performer
     */
    Performer createPerformer(PvmElement pvmElement, M performable);
}
