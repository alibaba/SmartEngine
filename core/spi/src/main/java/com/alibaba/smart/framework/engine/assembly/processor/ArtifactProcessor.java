package com.alibaba.smart.framework.engine.assembly.processor;

import com.alibaba.smart.framework.engine.assembly.processor.exception.ProcessorResolveException;
import com.alibaba.smart.framework.engine.core.LifeCycleListener;

import javax.xml.namespace.QName;

/**
 * Base interface for artifact processors.
 * Created by ettear on 16-4-12.
 */
public interface ArtifactProcessor<M> extends LifeCycleListener {

    /**
     * Resolve
     *
     * @param model   The model to resolve
     * @param context The context for the processor
     */
    void resolve(M model, ProcessorContext context) throws ProcessorResolveException;

    /**
     * Returns the type of artifact handled by this artifact processor.
     *
     * @return The type of artifact handled by this artifact processor
     */
    QName getArtifactType();

    /**
     * Returns the type of model handled by this artifact processor.
     *
     * @return The type of model handled by this artifact processor
     */
    Class<M> getModelType();
}
