package com.alibaba.smart.framework.engine.xml.parser;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.xml.parser.exception.ResolveException;

/**
 * Base interface for artifact parsers. Created by ettear on 16-4-12.
 */
public interface ArtifactParser<M> extends LifeCycleListener {

    /**
     * Resolve
     *
     * @param model The model to resolve
     * @param context The context for the parser
     */
    void resolve(M model, ParseContext context) throws ResolveException;

    /**
     * Returns the type of artifact handled by this artifact parser.
     *
     * @return The type of artifact handled by this artifact parser
     */
    QName getArtifactType();

    /**
     * Returns the type of model handled by this artifact parser.
     *
     * @return The type of model handled by this artifact parser
     */
    Class<M> getModelType();
}
