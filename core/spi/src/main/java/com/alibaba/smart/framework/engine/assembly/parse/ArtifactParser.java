package com.alibaba.smart.framework.engine.assembly.parse;

import com.alibaba.smart.framework.engine.assembly.Base;
import com.alibaba.smart.framework.engine.assembly.parse.exception.ResolveException;
import com.alibaba.smart.framework.engine.core.LifeCycleListener;

import javax.xml.namespace.QName;

/**
 * Base interface for artifact parsers.
 * Created by ettear on 16-4-12.
 */
public interface ArtifactParser<M> extends LifeCycleListener {

    /**
     * Resolve
     *
     * @param model   The model to resolve
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
