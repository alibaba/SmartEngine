package com.alibaba.smart.framework.engine.pvm;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.Transition;
import com.alibaba.smart.framework.engine.provider.ProviderRegister;

/**
 * Created by ettear on 16-4-13.
 */
public interface PvmTransition extends PvmInvocable<Transition>,ProviderRegister {

    PvmActivity getSource();

    PvmActivity getTarget();

    void execute(ExecutionContext context);

}
