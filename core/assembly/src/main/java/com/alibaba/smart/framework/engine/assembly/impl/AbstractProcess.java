package com.alibaba.smart.framework.engine.assembly.impl;

import com.alibaba.smart.framework.engine.assembly.Base;
import com.alibaba.smart.framework.engine.assembly.Process;
import lombok.Data;

import java.util.List;

/**
 * Created by ettear on 16-4-13.
 */
@Data
public abstract class AbstractProcess extends AbstractActivity implements Process {
    private List<Base> elements;
}
