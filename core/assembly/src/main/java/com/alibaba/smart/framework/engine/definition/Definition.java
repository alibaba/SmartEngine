package com.alibaba.smart.framework.engine.definition;

import com.alibaba.smart.framework.engine.assembly.Process;

/**
 * Created by ettear on 16-4-11.
 */
public interface Definition {

    Long getId();

    String getName();

    Integer getVersion();

    Process getProcess();
}
