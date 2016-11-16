package com.alibaba.smart.framework.engine.pvm.impl;

import com.alibaba.smart.framework.engine.model.assembly.IndentityElement;
import com.alibaba.smart.framework.engine.pvm.PvmInvocable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public abstract class AbstractPvmInvocable<M extends IndentityElement> implements PvmInvocable<M> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPvmInvocable.class);

    private M model;


    @Override
    public M getModel() {
        return this.model;
    }

    public void setModel(M model) {
        this.model = model;
    }

}
