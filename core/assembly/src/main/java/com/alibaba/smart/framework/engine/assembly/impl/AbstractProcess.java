package com.alibaba.smart.framework.engine.assembly.impl;

import com.alibaba.smart.framework.engine.assembly.Base;
import com.alibaba.smart.framework.engine.assembly.Process;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by ettear on 16-4-13.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractProcess extends AbstractActivity implements Process {
    private List<Base> elements;
}
