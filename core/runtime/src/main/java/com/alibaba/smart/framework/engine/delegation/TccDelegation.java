package com.alibaba.smart.framework.engine.delegation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;

public interface TccDelegation<T> extends JavaDelegation {

    TccResult<T> tryExecute(ExecutionContext executionContext);

    TccResult<T> confirmExecute(ExecutionContext executionContext);

    TccResult<T> cancelExecute(ExecutionContext executionContext);
}
