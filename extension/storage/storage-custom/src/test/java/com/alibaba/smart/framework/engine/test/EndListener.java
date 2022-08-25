package com.alibaba.smart.framework.engine.test;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.listener.Listener;
import com.alibaba.smart.framework.engine.pvm.event.EventConstant;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class EndListener implements Listener {

    @Override
    public void execute(EventConstant event,
                        ExecutionContext executionContext) {
        executionContext.getResponse().put("end","end_listener");
        executionContext.getResponse().put("endTime", System.currentTimeMillis());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
