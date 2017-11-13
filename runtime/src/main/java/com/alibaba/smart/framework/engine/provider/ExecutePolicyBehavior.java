package com.alibaba.smart.framework.engine.provider;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * @author ettear
 * Created by ettear on 14/10/2017.
 */
public interface ExecutePolicyBehavior {
    void enter(PvmActivity pvmActivity, ExecutionContext context);

    void execute(PvmActivity pvmActivity, ExecutionContext context);
}
