package com.alibaba.smart.framework.engine.pvm;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface PvmProcessInstance {

    ProcessInstance start(ExecutionContext executionContext);

    ProcessInstance signal(PvmActivity pvmActivity, ExecutionContext executionContext);

    /**
     * 进入节点
     *
     * @param pvmActivity
     * @param executionContext
     * @return
     */
    ProcessInstance enter(PvmActivity pvmActivity, ExecutionContext executionContext);
}
