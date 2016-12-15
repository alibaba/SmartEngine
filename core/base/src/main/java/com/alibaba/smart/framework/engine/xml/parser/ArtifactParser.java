package com.alibaba.smart.framework.engine.xml.parser;

import com.alibaba.smart.framework.engine.listener.LifeCycleListener;

import javax.xml.namespace.QName;


/**
 * Base interface for artifact parsers. Created by ettear on 16-4-12.
 */
public interface ArtifactParser<M> extends LifeCycleListener {



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
