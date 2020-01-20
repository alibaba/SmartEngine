package com.alibaba.smart.framework.engine.pvm.impl;

import com.alibaba.smart.framework.engine.model.assembly.ExtensionBasedElement;
import com.alibaba.smart.framework.engine.pvm.PvmElement;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public abstract class AbstractPvmElement<M extends ExtensionBasedElement> implements PvmElement<M> {


    private M model;

    @Override
    public M getModel() {
        return this.model;
    }

    public void setModel(M model) {
        this.model = model;
    }


}
