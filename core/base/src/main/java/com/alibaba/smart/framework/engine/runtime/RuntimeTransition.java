package com.alibaba.smart.framework.engine.runtime;

import com.alibaba.smart.framework.engine.assembly.Transition;
import com.alibaba.smart.framework.engine.context.InstanceContext;

/**
 * Created by ettear on 16-4-13.
 */
public interface RuntimeTransition extends RuntimeInvocable<Transition> {

    RuntimeActivity getSource();

    RuntimeActivity getTarget();

    void execute(InstanceContext context);

}
