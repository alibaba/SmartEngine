package com.alibaba.smart.framework.engine.model.artifact;

import com.alibaba.smart.framework.engine.model.artifact.Indentity;

/**
 * Created by ettear on 16-4-11.
 */
public interface Transition extends Indentity {

    String getSourceRef();

    String getTargetRef();
}
