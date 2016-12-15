package com.alibaba.smart.framework.engine.model.assembly.impl;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by ettear on 16-4-13.
 */
@Data
@EqualsAndHashCode(callSuper = true)

public abstract class AbstractProcessDefinition extends AbstractBase implements ProcessDefinition {

    private static final long serialVersionUID = -1765647192018309663L;


    private String  name;

    private String  version;

    private com.alibaba.smart.framework.engine.model.assembly.Process process;
}
