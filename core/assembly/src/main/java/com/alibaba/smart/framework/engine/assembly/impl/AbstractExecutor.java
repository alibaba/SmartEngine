package com.alibaba.smart.framework.engine.assembly.impl;

import com.alibaba.smart.framework.engine.assembly.Executor;
import lombok.Data;

/**
 * Abstract Executor
 * Created by ettear on 16-4-13.
 */
@Data
public abstract class AbstractExecutor extends AbstractBase implements Executor {
    private String id;
}
