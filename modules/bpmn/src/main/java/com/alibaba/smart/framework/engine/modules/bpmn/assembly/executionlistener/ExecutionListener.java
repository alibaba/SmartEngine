package com.alibaba.smart.framework.engine.modules.bpmn.assembly.executionlistener;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.model.assembly.Extension;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractBase;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExecutionListener extends AbstractBase implements Extension {

    /**
     * 
     */
    private static final long serialVersionUID = -2881657965418926667L;

    private String            clazzName;
    private String            event;
    private String            type;

}
