package com.alibaba.smart.framework.engine.test;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.listener.Listener;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class EndListener implements Listener {

    @Override
    public void execute(ExecutionContext executionContext) {
        executionContext.getResponse().put("end","end_listener");
    }
}
