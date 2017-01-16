package com.alibaba.smart.framework.engine.provider;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface ActivityBehavior<M extends Activity> {

    void enter(PvmActivity runtimeActivity, ExecutionContext context);

    void leave(PvmActivity runtimeActivity, ExecutionContext context);


}
