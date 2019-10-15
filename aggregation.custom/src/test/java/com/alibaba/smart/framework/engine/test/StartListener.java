package com.alibaba.smart.framework.engine.test;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.listener.EventListener;
import com.alibaba.smart.framework.engine.test.cases.ExecutionListenerAndValueTest;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class StartListener implements EventListener {

    @Override
    public void execute(ExecutionContext executionContext) {
        executionContext.getResponse().put("start","start_listener");
    }
}
