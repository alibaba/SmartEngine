package com.alibaba.smart.framework.engine.xml.parser;

import com.alibaba.smart.framework.engine.listener.LifeCycleListener;

import javax.xml.namespace.QName;

/**
 * Base interface for artifact parsers. Created by ettear on 16-4-12.
 */
public interface BaseXmlParser<M> extends LifeCycleListener {




    QName getArtifactType();


    Class<M> getModelType();
}
