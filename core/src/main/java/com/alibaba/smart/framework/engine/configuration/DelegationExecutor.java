package com.alibaba.smart.framework.engine.configuration;


import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.Activity;

/**
 * Created by 高海军 帝奇 74394 on 2019 December  14:45.
 */
public interface DelegationExecutor {

    void execute(ExecutionContext context,Activity activity);

}
