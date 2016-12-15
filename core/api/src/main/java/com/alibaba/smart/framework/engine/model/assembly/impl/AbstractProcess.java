package com.alibaba.smart.framework.engine.model.assembly.impl;

import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by ettear on 16-4-13.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractProcess extends AbstractActivity implements com.alibaba.smart.framework.engine.model.assembly.Process {

    private static final long serialVersionUID = -1139915200717778375L;
    private List<BaseElement> elements;
}
