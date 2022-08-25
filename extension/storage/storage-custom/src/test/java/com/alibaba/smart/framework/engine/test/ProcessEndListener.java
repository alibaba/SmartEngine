package com.alibaba.smart.framework.engine.test;

import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.listener.Listener;
import com.alibaba.smart.framework.engine.pvm.event.EventConstant;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class ProcessEndListener implements Listener {

    private static AtomicInteger count = new AtomicInteger(0);

    @Override
    public void execute(EventConstant event,
                        ExecutionContext executionContext) {

        count.incrementAndGet();
        executionContext.getResponse().put("processEndTime", System.currentTimeMillis());
        executionContext.getResponse().put("processEndCount", count);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
