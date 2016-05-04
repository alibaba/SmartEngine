package com.alibaba.smart.framework.engine.context.storage;

import com.alibaba.smart.framework.engine.context.Fact;

/**
 * Created by ettear on 16-5-4.
 */
public interface FactStorage {
    void saveProcessFact(String processInstanceId, Fact fact);

    Fact findProcessFact(String processInstanceId);

    void saveActivityFact(String processInstanceId,String executionInstanceId,Fact fact);

    Fact findActivityFact(String processInstanceId,String executionInstanceId);
}
