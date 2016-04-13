package com.alibaba.smart.framework.engine.assembly.impl;

import com.alibaba.smart.framework.engine.assembly.Base;
import lombok.Data;

/**
 * Created by ettear on 16-4-12.
 */
@Data
public abstract class AbstractBase implements Base{
    boolean unresolved;
}
