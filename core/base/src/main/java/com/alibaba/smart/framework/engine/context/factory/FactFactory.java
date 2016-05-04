package com.alibaba.smart.framework.engine.context.factory;

import com.alibaba.smart.framework.engine.context.Fact;

import java.util.Map;

/**
 * Created by ettear on 16-5-4.
 */
public interface FactFactory {
    Fact create();
    Fact create(Map<String,Object> variables);
}
