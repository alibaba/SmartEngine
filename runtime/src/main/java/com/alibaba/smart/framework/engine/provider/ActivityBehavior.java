package com.alibaba.smart.framework.engine.provider;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface ActivityBehavior {

    boolean enter(ExecutionContext context);

    void execute(ExecutionContext context);

    void leave(PvmActivity pvmActivity,ExecutionContext context);

}
