package com.alibaba.smart.framework.engine.persister.memory;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.instance.storage.VariableInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;

/**
 * Created by 高海军 帝奇 74394 on 2017 October  16:55.
 */
public class MemoryVariableInstanceStorage implements VariableInstanceStorage {

    @Override
    public void insert(VariablePersister variablePersister, VariableInstance variableInstance) {

    }

    @Override
    public List<VariableInstance> findList(Long processInstanceId, Long executionInstanceId,
                                           VariablePersister variablePersister) {
        return null;
    }
}
