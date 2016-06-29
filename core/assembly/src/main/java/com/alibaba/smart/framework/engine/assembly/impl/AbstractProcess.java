package com.alibaba.smart.framework.engine.assembly.impl;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.assembly.BaseElement;
import com.alibaba.smart.framework.engine.assembly.Process;

/**
 * Created by ettear on 16-4-13.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractProcess extends AbstractActivity implements Process {

    private static final long serialVersionUID = -1139915200717778375L;
    private List<BaseElement> elements;
}
