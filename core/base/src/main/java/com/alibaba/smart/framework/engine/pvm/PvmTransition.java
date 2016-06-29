package com.alibaba.smart.framework.engine.pvm;

import com.alibaba.smart.framework.engine.assembly.Transition;
import com.alibaba.smart.framework.engine.context.InstanceContext;

/**
 * Created by ettear on 16-4-13.
 */
public interface PvmTransition extends PvmInvocable<Transition> {

    PvmActivity getSource();

    PvmActivity getTarget();

    void execute(InstanceContext context);

}
