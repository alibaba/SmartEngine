package com.alibaba.smart.framework.engine.pvm;

import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.model.assembly.Transition;

/**
 * Created by ettear on 16-4-13.
 */
public interface PvmTransition extends PvmInvocable<Transition> {

    PvmActivity getSource();

    PvmActivity getTarget();

    void execute(InstanceContext context);

}
