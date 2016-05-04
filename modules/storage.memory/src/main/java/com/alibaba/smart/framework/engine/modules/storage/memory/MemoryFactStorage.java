package com.alibaba.smart.framework.engine.modules.storage.memory;

import com.alibaba.smart.framework.engine.context.Fact;
import com.alibaba.smart.framework.engine.context.storage.FactStorage;

/**
 * Created by ettear on 16-5-4.
 */
public class MemoryFactStorage implements FactStorage {

    @Override
    public void saveProcessFact(String processInstanceId, Fact fact) {

    }

    @Override
    public Fact findProcessFact(String processInstanceId) {
        return null;
    }

    @Override
    public void saveActivityFact(String processInstanceId, String activityInstanceId, Fact fact) {

    }

    @Override
    public Fact findActivityFact(String processInstanceId, String activityInstanceId) {
        return null;
    }
}
