package com.alibaba.smart.framework.engine.delegation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;

/**
 * @author 高海军 帝奇  2016.11.11
 */
public interface TccDelegation {

    //TODO 关于节点上的可执行action/event 是否需要配置到流程定义上面? 

    TccResult tryExecute(ExecutionContext executionContext);

    TccResult confirmExecute(ExecutionContext executionContext);

    TccResult cancelExecute(ExecutionContext executionContext);
}
