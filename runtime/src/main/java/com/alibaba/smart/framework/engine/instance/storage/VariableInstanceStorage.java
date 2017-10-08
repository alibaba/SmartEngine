package com.alibaba.smart.framework.engine.instance.storage;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;

public interface VariableInstanceStorage {

    void insert(VariablePersister variablePersister, VariableInstance variableInstance);

    List<VariableInstance> findList(Long processInstanceId, Long executionInstanceId,
                                    VariablePersister variablePersister);

}
