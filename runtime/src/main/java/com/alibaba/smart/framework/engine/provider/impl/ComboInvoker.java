package com.alibaba.smart.framework.engine.provider.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.provider.Invoker;
import com.alibaba.smart.framework.engine.provider.Performer;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class ComboInvoker implements Invoker {

    private Map<String, List<Performer>> performers;

    @Override
    public Object invoke(String method, ExecutionContext context) {
        Object result = null;
        if (null != this.performers) {
            List<Performer> methodPerformers = this.performers.get(method);
            if (null != methodPerformers) {
                for (Performer performer : methodPerformers) {
                    result = performer.perform(context);
                }
            }
        }
        return result;
    }

    public void addPerformer(String method, Performer performer) {
        if (null == this.performers) {
            this.performers = new HashMap<String, List<Performer>>();
        }
        List<Performer> methodPerformers = this.performers.get(method);
        if (null == methodPerformers) {
            methodPerformers = new ArrayList<Performer>();
            this.performers.put(method, methodPerformers);
        }
        methodPerformers.add(performer);
    }
}
