package com.alibaba.smart.framework.engine.listener;

import com.alibaba.smart.framework.engine.context.ExecutionContext;

/**
 * Created by 高海军 帝奇 74394 on  2019-09-01 11:51.
 */
public interface Listener {

    void execute(ExecutionContext executionContext);

}