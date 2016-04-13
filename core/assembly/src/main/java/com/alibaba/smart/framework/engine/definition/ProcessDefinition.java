package com.alibaba.smart.framework.engine.definition;

import com.alibaba.smart.framework.engine.assembly.Base;
import com.alibaba.smart.framework.engine.assembly.Process;

/**
 * Created by ettear on 16-4-11.
 */
public interface ProcessDefinition extends Base {

    String getId();

    String getName();

    String getVersion();

    Process getProcess();
}
