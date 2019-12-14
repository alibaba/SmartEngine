package com.alibaba.smart.framework.engine.test.delegation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.listener.EventListener;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class TccTracker implements EventListener {


    @Override
    public void execute(ExecutionContext executionContext) {
        String text = (String)executionContext.getRequest().get("text");

        executionContext.getResponse().put("hello1",text);

    }


}
