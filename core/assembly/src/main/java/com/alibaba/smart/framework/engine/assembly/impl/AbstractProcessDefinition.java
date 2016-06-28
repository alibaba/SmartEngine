package com.alibaba.smart.framework.engine.assembly.impl;

import lombok.Data;

import com.alibaba.smart.framework.engine.assembly.Process;
import com.alibaba.smart.framework.engine.assembly.ProcessDefinition;

/**
 * Created by ettear on 16-4-13.
 */
@Data
public abstract class AbstractProcessDefinition extends AbstractBase implements ProcessDefinition {

    private String  id;

    private String  name;

    private String  version;

    private Process process;
}
