package com.alibaba.smart.framework.engine.delegation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;

/**
 * @author 高海军 帝奇  2016.11.11
 */
public interface JavaDelegation {
    Object execute(ExecutionContext executionContext);

}
