package com.alibaba.smart.framework.engine.model.assembly.impl;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@Data
@EqualsAndHashCode(callSuper = true)

public abstract class AbstractProcessDefinition extends AbstractElement implements ProcessDefinition {

    private static final long serialVersionUID = -1765647192018309663L;


    private String name;

    private String version;

    private com.alibaba.smart.framework.engine.model.assembly.Process process;
}
