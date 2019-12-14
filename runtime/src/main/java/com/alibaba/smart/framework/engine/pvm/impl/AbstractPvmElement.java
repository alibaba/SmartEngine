package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionBasedElement;
import com.alibaba.smart.framework.engine.pvm.PvmElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public abstract class AbstractPvmElement<M extends ExtensionBasedElement> implements PvmElement<M> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPvmElement.class);

    private M model;

     

    protected ExtensionPointRegistry extensionPointRegistry;

    protected AbstractPvmElement(ExtensionPointRegistry extensionPointRegistry){
        this.extensionPointRegistry=extensionPointRegistry;
    }

    @Override
    public M getModel() {
        return this.model;
    }

    public void setModel(M model) {
        this.model = model;
    }


}
