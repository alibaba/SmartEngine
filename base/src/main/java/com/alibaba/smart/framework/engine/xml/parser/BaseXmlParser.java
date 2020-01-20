package com.alibaba.smart.framework.engine.xml.parser;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.hook.LifeCycleHook;

/**
 * Base interface for artifact parsers. Created by ettear on 16-4-12.
 */
public interface BaseXmlParser<M> extends LifeCycleHook {


    // check 是否需要
    QName getQname();

    Class<M> getModelType();
    
}
