package com.alibaba.smart.framework.engine.provider;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * Created by ettear on 16-4-11.
 */
public interface ActivityBehaviorProvider<M extends Activity> extends InvokerProvider{

    void execute(PvmActivity runtimeActivity,ExecutionContext context);

}
