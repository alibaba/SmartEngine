package com.alibaba.smart.framework.engine.delegation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;

public interface TccDelegation<T> extends JavaDelegation {

    //TODO 关于节点上的可执行action/event 是否需要配置到流程定义上面? 
    
    TccResult<T> tryExecute(ExecutionContext executionContext);

    TccResult<T> confirmExecute(ExecutionContext executionContext);

    TccResult<T> cancelExecute(ExecutionContext executionContext);
}
