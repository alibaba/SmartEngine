package com.alibaba.smart.framework.engine.test.delegation.event;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.listener.Listener;
import com.alibaba.smart.framework.engine.pvm.event.EventConstant;
import com.alibaba.smart.framework.engine.test.cases.CamundaEventTest;

public class ActivityEndListener  implements Listener {
    @Override
    public void execute(EventConstant event,
                        ExecutionContext executionContext) {

        CamundaEventTest.container.add("ActivityEndListener");
    }
}