package com.alibaba.smart.framework.process.behavior;

import com.alibaba.smart.framework.engine.context.ExecutionContext;

/**
 * @author 高海军 帝奇
 */
public interface ActivityBehavior {

    /**
     * 从上一个节点进入到本节点时,会执行该方法.
     */
    void execute(ExecutionContext executionContext);

    /**
     * 从当前节点出发,使流程往后续节点流动时,会执行该方法.
     */
    void signal();
}
