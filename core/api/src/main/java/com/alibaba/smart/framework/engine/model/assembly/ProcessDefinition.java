package com.alibaba.smart.framework.engine.model.assembly;

/**
 * Created by ettear on 16-4-11.
 */
public interface ProcessDefinition extends BaseElement {

    String getId();

    String getName();

    String getVersion();

    Process getProcess();
}
