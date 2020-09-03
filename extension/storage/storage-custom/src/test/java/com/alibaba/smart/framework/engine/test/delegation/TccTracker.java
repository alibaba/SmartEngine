package com.alibaba.smart.framework.engine.test.delegation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.listener.Listener;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class TccTracker implements Listener {


    @Override
    public void execute(PvmEventConstant event,
                        ExecutionContext executionContext) {
        String text = (String)executionContext.getRequest().get("text");

        executionContext.getResponse().put("hello1",text);

    }


}
