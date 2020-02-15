package com.alibaba.smart.framework.engine.delegation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;

/**
 * @author 高海军 帝奇  2016.11.11
 *
 * use `JavaDelegation` or  `ContextBoundedJavaDelegation` instead.
 */
@Deprecated
public interface TccDelegation {

    TccResult tryExecute(ExecutionContext executionContext);

    TccResult confirmExecute(ExecutionContext executionContext);

    TccResult cancelExecute(ExecutionContext executionContext);
}
