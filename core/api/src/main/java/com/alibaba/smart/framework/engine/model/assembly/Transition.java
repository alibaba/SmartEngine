package com.alibaba.smart.framework.engine.model.assembly;


/**
 * Created by ettear on 16-4-11.
 */
public interface Transition extends IndentityElement {

    String getSourceRef();

    String getTargetRef();
}
